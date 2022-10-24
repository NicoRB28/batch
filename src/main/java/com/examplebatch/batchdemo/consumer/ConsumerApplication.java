package com.examplebatch.batchdemo.consumer;

import com.examplebatch.batchdemo.listener.JobCompletionNotificationListener;
import com.examplebatch.batchdemo.model.Person;
import com.examplebatch.batchdemo.processor.PersonItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    private final KafkaProperties kafkaProperties;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    JobCompletionNotificationListener listener(JdbcTemplate jdbcTemplate) {
        return new JobCompletionNotificationListener(jdbcTemplate);
    }

    @Bean
    Job job() {
        return this.jobBuilderFactory
                .get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener(null))
                .start(start())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (person_id, first_name, last_name) VALUES (:personId, :firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    KafkaItemReader<Integer, Person> kafkaItemReader() {
        var properties = new Properties();
        properties.putAll(this.kafkaProperties.buildConsumerProperties());
        return new KafkaItemReaderBuilder<Integer, Person>()
                .partitions(0)
                .consumerProperties(properties)
                .name("persons-reader")
                .saveState(true)
                .topic("persons")
                .build();
    }

    @Bean
    public PersonItemProcessor processor(){
        return new PersonItemProcessor();
    }

    @Bean
    Step start() {
        return this.stepBuilderFactory
                .get("step")
                .<Person, Person>chunk(10)
                .writer(writer(null))
                .processor(processor())
                .reader(kafkaItemReader())
                .build();
    }
}
