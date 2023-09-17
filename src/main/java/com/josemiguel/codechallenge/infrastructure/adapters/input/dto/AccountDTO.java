package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AccountDTO {

	private Long accountId;
	private BigDecimal balance;
	private String accountName;
	private String iban;
	private String currency;
	private Long version;

}
