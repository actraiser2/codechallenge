package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig extends DefaultBatchConfigurer {

	@Autowired JobLauncher jobLauncher;

	@Override
	public JobLauncher getJobLauncher() {
		// TODO Auto-generated method stub
		var jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(taskExecutor2());
		jobLauncher.setJobRepository(getJobRepository());
		return jobLauncher;
	}
	
	
	@Bean
	public TaskExecutor taskExecutor2() {
		var executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		
		return executor;
	}
}
