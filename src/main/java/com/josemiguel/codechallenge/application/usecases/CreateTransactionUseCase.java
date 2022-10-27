package com.josemiguel.codechallenge.application.usecases;

import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;

public interface CreateTransactionUseCase {

	public void createTransaction(CreateTransactionCommand command);
}
