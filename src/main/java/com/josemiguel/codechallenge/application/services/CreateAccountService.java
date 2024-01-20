package com.josemiguel.codechallenge.application.services;

import javax.persistence.EntityManagerFactory;
import javax.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josemiguel.codechallenge.application.ports.input.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.ports.output.AccountRepository;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.domain.model.events.AccountCreatedEvent;
import com.josemiguel.codechallenge.infrastructure.adapters.output.amqp.AmqpEventPub;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
class CreateAccountService implements CreateAccountUseCase {

	private final AccountRepository accountRepository;
	private final EntityManagerFactory emf;
	private final Validator validator;
	private final AmqpEventPub amqpEventPub;
	
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
		
		amqpEventPub.sendEventAccountCreated(
				AccountCreatedEvent.builder().
				accountId(accountId).
				iban(command.iban()).
				balance(0d).
				build());
		
		//if (true) throw new NotFoundAccoundExeption(accountId);
		
		
		return accountId;
		
		
	
	}

}
