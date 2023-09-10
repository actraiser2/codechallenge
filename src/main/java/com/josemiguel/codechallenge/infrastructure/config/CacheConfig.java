package com.josemiguel.codechallenge.infrastructure.config;


import java.time.Duration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {

	@Bean
	public Cache<String, String> codeChallengeCacheConfig(){
		return Caffeine.newBuilder().maximumSize(100).expireAfterWrite(Duration.ofMinutes(10)).
				recordStats().build();
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
	    return new Jackson2JsonMessageConverter();
	}
}
