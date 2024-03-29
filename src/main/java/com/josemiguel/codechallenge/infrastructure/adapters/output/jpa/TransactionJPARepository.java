package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import java.util.Optional;

import com.josemiguel.codechallenge.application.ports.output.TransactionRepository;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;

public interface TransactionJPARepository extends TransactionRepository {

	public Optional<Transaction> findByReference(String reference);

}
