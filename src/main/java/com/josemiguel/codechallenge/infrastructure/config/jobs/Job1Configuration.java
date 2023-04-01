package com.josemiguel.codechallenge.infrastructure.config.jobs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryOutputPort;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Job1Configuration {

	@Autowired JobBuilderFactory jobBuilderFactory;
	@Autowired StepBuilderFactory stepBuilderFactory;
	@Autowired JobRepository jobRepository;
	@Autowired AccountRepositoryOutputPort accountRepository;
	@Autowired ResourceLoader resourceLoader;
	
	@Bean
	public Job job1() {
		return jobBuilderFactory.get("job1").
				
				incrementer(new RunIdIncrementer()).
				start(step1()).
				next(step2()).
				next(step3()).
				build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").tasklet((step, chunkContext ) -> {
			var stopwatch = new StopWatch();
			stopwatch.start();
			
			//log.info("Job1 - Step1 !!!!!!!!:" + chunkContext.getStepContext().getJobParameters());
			String timestamp = chunkContext.getStepContext().
					getJobParameters().get("timestamp") + "-";
		
			var command = CreateAccountCommand.builder().accountName("Josemi - " + timestamp).iban("ES00000001").build();
			accountRepository.save(new Account(command));
			
			stopwatch.stop();
			step.getStepExecution().getJobExecution().getExecutionContext().put("executionTimeDuration", stopwatch.getTotalTimeSeconds());
			return RepeatStatus.FINISHED;
		}).build(); 
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").tasklet((stepCont, chunkContext ) -> {
			//log.info("Job1 - Step2  !!!!!!!!:" +  chunkContext.getStepContext().getJobParameters());
			String timestamp = chunkContext.getStepContext().
					getJobParameters().get("timestamp") + "";
			//log.info("Step2 Contribution:" + stepCont.getStepExecution().getExecutionContext());
			var command = CreateAccountCommand.builder().accountName("Alex - " + timestamp).iban("ES00000001").build();
			accountRepository.save(new Account(command));
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	@Bean 
	public Step step3() {
		return stepBuilderFactory.get("step3").chunk(50).
				reader(itemReader(null)).
				writer(items -> {
					items.stream().
						forEach(l -> {
							var command = CreateAccountCommand.builder().
									accountName(StringUtils.substring(l.toString(), 0, 255)).
									iban("ES00000001").
									build();
							accountRepository.save(new Account(command));
						});
				}).
				build();
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<String> itemReader(@Value("#{jobParameters['filePath']}") String filePath) {
		var itemReader = new FlatFileItemReaderBuilder<String>();
		itemReader.lineMapper(new PassThroughLineMapper());
		itemReader.resource(resourceLoader.getResource("file:/" + filePath));
		itemReader.name("flatFileItemReader");
		return itemReader.build();
	}
	
}
