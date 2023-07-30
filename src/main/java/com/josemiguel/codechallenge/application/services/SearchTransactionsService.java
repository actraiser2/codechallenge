package com.josemiguel.codechallenge.application.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.josemiguel.codechallenge.application.ports.input.SearchTransactionsUseCase;
import com.josemiguel.codechallenge.application.ports.output.TransactionRepositoryRepository;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.utils.Constants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class SearchTransactionsService implements SearchTransactionsUseCase {

	private TransactionRepositoryRepository transactionRepository;
	@Override
	public List<Transaction> searchTransactions(String iban, int sortByAmount) {
		
		List<Transaction> transactions = null;
		if (sortByAmount == 1 /*Classify in asc order*/) {
			transactions = transactionRepository.findTransactionsByAccountIban(iban, PageRequest.of(0, Constants.PAGE_SIZE, Sort.by(Sort.Order.asc("amount"))));
		}
		else if (sortByAmount == -1 /*Classify in desc order*/) {
			transactions = transactionRepository.findTransactionsByAccountIban(iban, PageRequest.of(0, Constants.PAGE_SIZE, Sort.by(Sort.Order.desc("amount"))));
		}
		
		return transactions;
		
	}

}
