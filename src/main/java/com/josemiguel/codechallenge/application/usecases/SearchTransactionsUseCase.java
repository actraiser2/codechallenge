package com.josemiguel.codechallenge.application.usecases;

import java.util.List;

import com.josemiguel.codechallenge.domain.model.entities.Transaction;

public interface SearchTransactionsUseCase {

	public List<Transaction> searchTransactions(String iban, int sortByAmount);
}
