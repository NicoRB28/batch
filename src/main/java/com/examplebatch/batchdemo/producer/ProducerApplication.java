package com.examplebatch.batchdemo.producer;

import com.examplebatch.batchdemo.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;

//@EnableBatchProcessing
//@SpringBootApplication
//@RequiredArgsConstructor
public class ProducerApplication {
/*    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final KafkaTemplate<Integer, Person> kafkaTemplate;
    private static final String QUERY = "SELECT user_id, first_name, last_name FROM users";

    @Bean
    Job job() {
        return this.jobBuilderFactory
                .get("job")
                .start(start())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    KafkaItemWriter<Integer, Person> kafkaItemWriter() {
        return new KafkaItemWriterBuilder<Integer, Person>()
                .kafkaTemplate(kafkaTemplate)
                .itemKeyMapper(Person::getPersonId)
                .build();
    }

    @Bean
    Step start() {
        return this.stepBuilderFactory
                .get("s1")
                .<Person, Person>chunk(10)
                .reader(reader(null))
                .writer(kafkaItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Person> reader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Person>()
                .name("cursorItemReader")
                .dataSource(dataSource)
                .sql(QUERY)
                .rowMapper((rs, rowNum) -> new Person(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)))
                .build();
    }*/
}
