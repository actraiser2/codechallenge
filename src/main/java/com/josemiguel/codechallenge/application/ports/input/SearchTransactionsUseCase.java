package com.josemiguel.codechallenge.application.ports.input;

import java.util.List;

import com.josemiguel.codechallenge.domain.model.entities.Transaction;

public interface SearchTransactionsUseCase {

	public List<Transaction> searchTransactions(String iban, int sortByAmount);
}
