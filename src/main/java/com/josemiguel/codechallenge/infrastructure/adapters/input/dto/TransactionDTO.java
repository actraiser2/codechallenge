package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

	private String reference;
	private ZonedDateTime date;
	private Double amount;
	private BigDecimal fee;
	private String description;
	private TransactionStatus status;
}
