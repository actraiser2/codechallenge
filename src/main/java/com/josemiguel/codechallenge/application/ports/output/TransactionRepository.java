package com.josemiguel.codechallenge.application.ports.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.josemiguel.codechallenge.domain.model.entities.Transaction;

@NoRepositoryBean
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	public Optional<Transaction> findByReference(String reference);
	
	public Optional<List<Transaction>> findTransactionsByAccountIban(String iban, 
			Pageable pageable);
	
	public List<Transaction> findAll();
}
