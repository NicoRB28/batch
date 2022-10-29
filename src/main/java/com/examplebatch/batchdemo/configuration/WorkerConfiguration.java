package com.examplebatch.batchdemo.configuration;

import com.examplebatch.batchdemo.model.Person;
import com.examplebatch.batchdemo.processor.PersonItemProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("worker")
public class WorkerConfiguration {
    private static final int CHUNK_SIZE = 10;
    private static final int WAITING_TIME = 3000;

    public final DataSource dataSource;
    private final RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;

    public WorkerConfiguration(DataSource dataSource,
                               RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory) {
        this.dataSource = dataSource;
        this.workerStepBuilderFactory = workerStepBuilderFactory;
    }

    @Bean
    public DirectChannel repliesFromWorkers() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(KafkaTemplate<Integer, Person> kafkaTemplate) {
        return IntegrationFlows.from(repliesFromWorkers())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic("repliesFromWorkers"))
                .route("repliesFromWorkers")
                .get();
    }

    @Bean
    public DirectChannel requestForWorkers() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConsumerFactory<Integer, Person> consumerFactory) {
        return IntegrationFlows
                .from(Kafka.inboundChannelAdapter(consumerFactory, new ConsumerProperties("requestForWorkers")))
                .channel(requestForWorkers())
                .get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

    @Bean
    public Step workerStep() {
        SimpleStepBuilder<Person, Person> workerStepBuilder = workerStepBuilderFactory.get("workerStep")
                .inputChannel(requestForWorkers())
                .outputChannel(repliesFromWorkers())
                .<Person, Person> chunk(CHUNK_SIZE)
                .reader(pagingItemReader(null, null))
                .processor(itemProcessor())
                .processor((ItemProcessor<? super Person, ? extends Person>) transaction -> {
                    System.out.println("PROCESANDO = " + transaction);
                    return transaction;
                })
                .writer(personItemWriter());
        return workerStepBuilder.build();
    }

    /*@Bean
    @StepScope
    public JdbcPagingItemReader<Person> pagingItemReader(@Value("#{stepExecutionContext['minValue']}") Long minValue,
                                                         @Value("#{stepExecutionContext['maxValue']}")Long maxValue) {
        System.out.println("Reading " + minValue + " to " + maxValue);
        JdbcPagingItemReader<Person> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.dataSource);
        reader.setFetchSize(1000);
        reader.setRowMapper(new PersonRowMapper());//TODO COMPLETAR

        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause();//TODO COMPLETAR
        queryProvider.setFromClause();//TODO COMPLETAR
        queryProvider.setWhereClause("where id >= " + minValue + " and id <" + maxValue);

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);
        return reader;
    }*/

    @Bean
    public JdbcPagingItemReader<Person> pagingItemReader(@Value("#{stepExecutionContext['minValue']}") Long minValue,
                                                         @Value("#{stepExecutionContext['maxValue']}")Long maxValue) {
        return new JdbcPagingItemReaderBuilder<Person>()
                .name("pagingItemReader")
                .dataSource(dataSource)
                .fetchSize(5)
                .pageSize(5)
                .rowMapper(((rs, rowNum) -> new Person(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                )))
                .queryProvider(queryProvider(minValue, maxValue))
                .build();
    }

    private PostgresPagingQueryProvider queryProvider(Long minValue, Long maxValue) {
        final Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("user_id", Order.ASCENDING);
        final PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("user_id, first_name, last_name");
        queryProvider.setFromClause("users");
        queryProvider.setWhereClause("where id >= " + minValue + " and id <" + maxValue);
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }



    /*//TODO CAMBIAR
    @Bean
    @StepScope
    public ItemProcessor<Person, Person> itemProcessor() {
        return item -> {
            Thread.sleep(WAITING_TIME);
            System.out.println(item);
            return item;
        };
    }*/

    @Bean
    @StepScope
    public PersonItemProcessor itemProcessor() {
        return new PersonItemProcessor();
    }

    //TODO CAMBIAR
    /*@Bean
    @StepScope
    public ItemWriter<Person> personItemWriter() {
        return items -> {
            System.out.printf("%d items where written\\n", items.size() );
        };
    }*/

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Person> personItemWriter() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (person_id, first_name, last_name) VALUES (:personId, :firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

}
