package com.examplebatch.batchdemo.configuration;

import com.examplebatch.batchdemo.listener.JobCompletionNotificationListener;
import com.examplebatch.batchdemo.model.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
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


@Profile("!worker")
@Configuration
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final JobExplorer jobExplorer;
    private final RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
                            JobExplorer jobExplorer,
                            RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory,
                            JobCompletionNotificationListener jobCompletionNotificationListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.jobExplorer = jobExplorer;
        this.managerStepBuilderFactory = managerStepBuilderFactory;
        this.jobCompletionNotificationListener = jobCompletionNotificationListener;
    }

    /*@Bean
    public JobCompletionNotificationListener jobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        return new JobCompletionNotificationListener(jdbcTemplate);
    }*/

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(managerStep())
                .listener(jobCompletionNotificationListener)
                .build();
    }

    @Bean
    public Step managerStep() {
        return managerStepBuilderFactory.get("managerStep")
                .partitioner("workerStep", rangePartitioner(null, null))
                .outputChannel(requestForWorkers())
                .inputChannel(repliesFromWorkers())
                .jobExplorer(jobExplorer)
                .build();
    }

    @Bean
    @StepScope
    public Partitioner rangePartitioner(@Value("#{jobParameters['minId']}")Integer minId,
                                        @Value("#{jobParameters['maxId']}")Integer maxId) {
        return new PersonIdRangePartitioner(minId, maxId);
    }

    @Bean
    public DirectChannel requestForWorkers() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outBoundFlow(KafkaTemplate kafkaTemplate) {
        return IntegrationFlows
                .from(requestForWorkers())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic("requestForWorkers"))
                .route("requestForWorkers")
                .get();
    }

    @Bean
    public DirectChannel repliesFromWorkers() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConsumerFactory<Integer, Person> consumerFactory) {
        return IntegrationFlows
                .from(Kafka.inboundChannelAdapter(consumerFactory,
                        new ConsumerProperties("repliesFromWorkers")))
                .channel(repliesFromWorkers())
                .get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

}
