package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ForecastDTO {

	private double longitude;
	private double latitude;
}
