package com.josemiguel.codechallenge.application.usecases;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;

public interface CreateAccountUseCase {

	public void createAccount(CreateAccountCommand command);
}
