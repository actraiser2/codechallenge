package com.josemiguel.codechallenge.application.ports.input;

import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;

public interface CreateTransactionUseCase {

	public void createTransaction(CreateTransactionCommand command);
}
