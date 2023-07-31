package com.josemiguel.codechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan("com.josemiguel.codechallenge.infrastructure.config")
@EnableJpaAuditing
public class CodechallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodechallengeApplication.class, args);
	} 

}
