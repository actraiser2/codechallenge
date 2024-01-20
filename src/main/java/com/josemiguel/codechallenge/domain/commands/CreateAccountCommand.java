package com.josemiguel.codechallenge.domain.commands;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.josemiguel.codechallenge.infrastructure.errors.exceptions.AccountValidationException;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record CreateAccountCommand(@NotEmpty String accountName,
	@NotEmpty String iban, @NotEmpty String holderName, 
	@Max(100) @Min(20) Integer age) {

	public CreateAccountCommand{
		if (iban.length() < 10) {
			throw new AccountValidationException();
		}
	}
}
