package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
	private Integer timezone;
	private Long id;
	private String name;
	private Integer cod;
	
	private MainWeather main;
	private Integer responseCode;
	private String errorMessage;
}
