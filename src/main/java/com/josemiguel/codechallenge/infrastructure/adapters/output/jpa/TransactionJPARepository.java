package com.josemiguel.codechallenge.infrastructure.adapters.output.jpa;

import java.util.Optional;

import com.josemiguel.codechallenge.application.ports.output.TransactionRepositoryOutputPort;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;

public interface TransactionJPARepository extends TransactionRepositoryOutputPort {

	public Optional<Transaction> findByReference(String reference);

}
