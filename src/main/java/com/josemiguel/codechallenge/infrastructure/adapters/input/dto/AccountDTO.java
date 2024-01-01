package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class AccountDTO {
	@NotNull 
	private Long accountId;
	@NotNull 
	private BigDecimal balance;
	private String accountName;
	private String iban;
	private String currency;
	private Long version;
	private String holderName;

}
