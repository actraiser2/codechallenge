package com.josemiguel.codechallenge;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.josemiguel.codechallenge.infrastructure.config.properties")
@EnableTask
@Slf4j
public class CodechallengeApplication {

	public static void main(String... args) {
		SpringApplication.run(CodechallengeApplication.class, args);
	}

	@Bean
	public CommandLineRunner taskConfiguration(JobLauncher jobLauncher, Job job1) {
		return args -> {
			log.info("Executing my first task");
			var parameters = new JobParametersBuilder().addDate("timestamp", new Date()).
				addString("filePath", "c:/tmp/aggregation.log").toJobParameters();
			
			jobLauncher.run(job1, parameters);
			
			
		};
	}

}
