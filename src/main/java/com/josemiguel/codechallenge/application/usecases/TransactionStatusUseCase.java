package com.josemiguel.codechallenge.application.usecases;

import java.util.Optional;

import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.domain.model.valueobjects.Channel;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;

public interface TransactionStatusUseCase {

	public Optional<Transaction> getTransactionStatus(TransactionStatusRequestDTO request);
	
	public TransactionStatus applyBusinessRule(Optional<Transaction> transaction, Channel channel);
}
