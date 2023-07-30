package com.josemiguel.codechallenge.application.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.josemiguel.codechallenge.application.ports.input.CreateTransactionUseCase;
import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryRepository;
import com.josemiguel.codechallenge.application.ports.output.TransactionRepositoryRepository;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.ExistingTransactionException;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.NotFoundAccoundExeption;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
class CreateTransactionService implements CreateTransactionUseCase {

	private AccountRepositoryRepository accountRepository;
	private TransactionRepositoryRepository transactionRepository;
	
	@Override
	public void createTransaction(CreateTransactionCommand command) {
		// TODO Auto-generated method stub
		log.info("Creating new transaction:" + command);
		
		
		//Checking if exists the account in the database
		var account = accountRepository.findByIban(command.getIban());
		if (account.isPresent()) {
			
			//Checking if exists the transaction in the database if reference provided
			if (StringUtils.isNotEmpty(command.getReference())){
				Optional<Transaction> transaction = transactionRepository.findByReference(command.getReference());
				if (transaction.isPresent()) {
					throw new ExistingTransactionException("Transaction " + command.getReference() + " is already loaded in the system");
				}
			}
			
			Transaction newTransaction = account.get().addTransaction(command);
			transactionRepository.save(newTransaction);
			
			
		}
		else {
			throw new NotFoundAccoundExeption("Iban " + command.getIban() + " was not found");
		}
	}

}
