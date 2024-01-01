package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TransactionDTO {

	private String reference;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime date;
	private BigDecimal amount;
	private BigDecimal fee;
	private String description;
	private TransactionStatus status;
}
