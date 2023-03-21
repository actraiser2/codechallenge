package com.josemiguel.codechallenge.infrastructure.functions;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FunctionsConfiguration {

	@Bean
	public Supplier<String> doSomething(){
		return () -> {
			return "Returning my first function";
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
