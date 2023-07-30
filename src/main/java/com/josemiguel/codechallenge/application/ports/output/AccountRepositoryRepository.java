package com.josemiguel.codechallenge.application.ports.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;

@NoRepositoryBean
public interface AccountRepositoryRepository extends CrudRepository<Account, Long>{

	
	public List<Account> findAll();
	public Optional<Account> findByIban(String iban);
}
