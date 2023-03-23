package com.josemiguel.codechallenge.infrastructure.functions;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FunctionsConfiguration {

	@Autowired Environment env;
	
	@Bean
	public Supplier<String> doSomething(){
		return () -> {
			return "Returning my first function (" + env.getProperty("app.version") + ")";
		};
	}
	
	@Bean
	public Function<String, String> doSomething2(){
		return c -> {
			log.info("function:" + c);
			return c;
		};
	}
}
