package com.josemiguel.codechallenge.application.ports.output;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;

@NoRepositoryBean
public interface AccountRepositoryOutputPort extends CrudRepository<Account, Long>{

	public Optional<Account> findByIban(String iban);
}
