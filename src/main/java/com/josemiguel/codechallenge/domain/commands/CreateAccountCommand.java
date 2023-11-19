package com.josemiguel.codechallenge.domain.commands;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;

@Builder
public record CreateAccountCommand(@NotEmpty String accountName,
	@NotEmpty String iban, @NotEmpty String holderName, Integer age) {

	public CreateAccountCommand{
		if (iban.length() < 10) {
			throw new IllegalArgumentException("The iban can not be less than 10");
		}
	}
}
