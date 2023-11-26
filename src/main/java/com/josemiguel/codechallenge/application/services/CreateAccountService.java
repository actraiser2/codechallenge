package com.josemiguel.codechallenge.application.services;

import javax.persistence.EntityManagerFactory;
import javax.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josemiguel.codechallenge.application.ports.input.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.ports.output.AccountRepository;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
//@Transactional
@Slf4j
@AllArgsConstructor
class CreateAccountService implements CreateAccountUseCase {

	private AccountRepository accountRepository;
	private EntityManagerFactory emf;
	private Validator validator;
	
	@Override
	//@TimeLimiter(name = "createAccount" )
	@Transactional()
	public Long createAccount(CreateAccountCommand command) {
		// TODO Auto-generated method stub
		
		log.info("Creating new account:" + command);
		
		var constraints = validator.validate(command);
		
		log.info("Result validation:" + constraints);
		
		Account account = new Account(command);
		accountRepository.save(account);
		
		Long accountId = account.getAccountId();
		
		//if (true) throw new NotFoundAccoundExeption(accountId);
		
		
		return accountId;
		
		
	
	}

}
