package com.examplebatch.batchdemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@EnableBatchIntegration
@SpringBootApplication
public class BatchDemoApplication {

	public static void main(String[] args) {
		//System.exit(SpringApplication.exit(SpringApplication.run(BatchDemoApplication.class, args)));
		SpringApplication.run(BatchDemoApplication.class, args);
	}

}
