package com.josemiguel.codechallenge.domain.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTransactionCommand {

	private String reference;
	
	@NotEmpty @JsonProperty("account_iban") 
	private String iban;
	
	@JsonProperty("date") 
	private ZonedDateTime operationDate;
	
	@NotNull BigDecimal amount;
	
	private BigDecimal fee;
	
	private String description;
	
}
