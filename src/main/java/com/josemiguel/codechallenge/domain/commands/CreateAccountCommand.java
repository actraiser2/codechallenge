package com.josemiguel.codechallenge.domain.commands;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccountCommand {

	@NotEmpty private String accountName;
	@NotEmpty String iban;
}
