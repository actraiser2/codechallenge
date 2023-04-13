package com.josemiguel.codechallenge.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "weather")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
