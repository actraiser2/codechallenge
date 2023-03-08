package com.josemiguel.codechallenge.infrastructure.config;


import java.time.Duration;

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
}
