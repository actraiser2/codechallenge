package com.josemiguel.codechallenge.application.ports.input;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;

public interface CreateAccountUseCase {

	public Long createAccount(CreateAccountCommand command);
}
