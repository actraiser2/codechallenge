package com.josemiguel.codechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ConfigurationPropertiesScan("com.josemiguel.codechallenge.infrastructure.config")
@EnableJpaAuditing
@Slf4j
public class CodechallengeApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(CodechallengeApplication.class, args);
		var databaseUrl = context.getEnvironment().getProperty("spring.datasource.url");
		log.info("DATABASE URL:" + databaseUrl);
	} 

}
