package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TransactionDTO {

	private String reference;
	private LocalDateTime date;
	private BigDecimal amount;
	private BigDecimal fee;
	private String description;
	private TransactionStatus status;
}
