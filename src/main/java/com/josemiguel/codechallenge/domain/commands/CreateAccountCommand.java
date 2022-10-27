package com.josemiguel.codechallenge.domain.commands;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CreateAccountCommand {

	@NotEmpty private String accountName;
	@NotEmpty String iban;
}
