package com.josemiguel.codechallenge.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "weather")
@Data
public class WeatherConfigProperties {

	
	/**
	 * Url base for connecting the wearhtwr service
	 */
	private String url;
	
	/**
	 * ApiLey of the service
	 */
	private String apiKey;
}
