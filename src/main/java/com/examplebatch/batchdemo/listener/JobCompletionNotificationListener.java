package com.examplebatch.batchdemo.listener;

import com.examplebatch.batchdemo.model.Person;
import com.examplebatch.batchdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus().equals(BatchStatus.COMPLETED)){
            LOGGER.info("Job Finished, time to verify.");

            jdbcTemplate.query("SELECT person_id, first_name, last_name FROM people",
                    (rs, rowNum) -> new Person(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3)
                    )).forEach(person -> LOGGER.info("Found <{}> in database.",person));
        }
    }
}
