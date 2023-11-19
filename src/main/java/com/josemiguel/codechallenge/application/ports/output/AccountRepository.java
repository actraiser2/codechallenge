package com.josemiguel.codechallenge.application.ports.output;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;

@NoRepositoryBean
public interface AccountRepository extends CrudRepository<Account, Long>{

	
	public List<Account> findAll();
	public Optional<Account> findByIban(String iban);
	@Query("update Account a set a.balance=:balance where a.accountId=:accountId")
	@Modifying
	public void updateBalance(Long accountId, BigDecimal balance);
	public List<Account> findAllByOrderByAccountId();
};
