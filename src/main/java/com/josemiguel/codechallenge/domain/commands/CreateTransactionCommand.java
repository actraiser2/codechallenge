package com.josemiguel.codechallenge.domain.commands;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateTransactionCommand implements Serializable{

	private String reference;
	
	@NotEmpty @JsonProperty("account_iban") 
	private String iban;
	
	@JsonProperty("date") 
	private LocalDateTime operationDate;
	
	@NotNull BigDecimal amount;
	
	private BigDecimal fee;
	
	private String description;
	
}
