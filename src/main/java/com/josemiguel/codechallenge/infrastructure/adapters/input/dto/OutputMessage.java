package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputMessage {
	private String echo;
	private ZonedDateTime timestamp;
}
