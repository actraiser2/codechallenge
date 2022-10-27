package com.josemiguel.codechallenge.application.ports.input;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryOutputPort;
import com.josemiguel.codechallenge.application.usecases.CreateAccountUseCase;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CreateAccountInputPort implements CreateAccountUseCase {

	private AccountRepositoryOutputPort accountRepository;
	
	@Override
	public void createAccount(CreateAccountCommand command) {
		// TODO Auto-generated method stub
		log.info("Creating new account:" + command);
		
		Account account = new Account(command);
		accountRepository.save(account);
		
		log.info("Created !!!!!:" + account.getAccountId());
		
	}

}
