package com.josemiguel.codechallenge.infrastructure.adapters.input.web;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josemiguel.codechallenge.application.ports.input.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.ports.input.CreateTransactionUseCase;
import com.josemiguel.codechallenge.application.ports.input.SearchTransactionsUseCase;
import com.josemiguel.codechallenge.application.ports.input.TransactionStatusUseCase;
import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryRepository;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;
import com.josemiguel.codechallenge.infrastructure.config.props.KafkaConfigProperties;
import com.josemiguel.codechallenge.infrastructure.mappers.AccountMapper;
import com.josemiguel.codechallenge.infrastructure.mappers.TransactionMapper;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
@Tag(name = "Services available for the project code challenge")
@Slf4j
public class CodeChallengeController {

	private CreateAccountUseCase createAccountUseCase;
	private CreateTransactionUseCase createTransactionUseCase;
	private SearchTransactionsUseCase searchTransactionsUseCase;
	private TransactionStatusUseCase transactionStatusUseCase;
	private MeterRegistry meterRegistry;
	private Environment env;
	private AccountRepositoryRepository accountRepository;
	private List<KafkaConfigProperties> kafkaPropertiesList;
	private EntityManagerFactory emf;
	@PersistenceContext
	private final EntityManager entityManager;
	private RabbitTemplate rabbitTemplate;
	private TransactionMapper transactionMapper;
	private AccountMapper accountMapper;
	
	@PostMapping("/accounts")
	@Operation(description = "This method allows you to insert a new account")
	@Counted(description = "Number of accounts created")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) {
		createAccountUseCase.createAccount(command);
		//producerTemplate.sendBody("direct:restCodechallenge", command);
		meterRegistry.counter("accounts", "app", env.getProperty("app.version")).increment();
		//rabbitTemplate.convertAndSend("accounts", command);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/accounts", consumes = "*/*")
	@Operation(description = "This method lists all the accounts")
	public ResponseEntity<List<AccountDTO>> accounts() {
		
		var accounts = accountRepository.findAll();
		log.info("Accessing all accounts:" + accounts);
		return ResponseEntity.of(Optional.ofNullable(accountMapper.toListAccountDTO(accounts)));
	}
	
	@PostMapping("/transactions")
	@Operation(description = "This method allows you to insert a new movement")
	public ResponseEntity<Void> createTransaction(@RequestBody @Valid CreateTransactionCommand command) {
		createTransactionUseCase.createTransaction(command);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/transactions", consumes = "*/*")
	@Operation(description = "This method is used for querying movements using a couple filters")
	public List<TransactionDTO> searchTransactions(@RequestParam String iban, @RequestParam int sortByAmount) {
		
		var transactions = searchTransactionsUseCase.searchTransactions(iban, sortByAmount);
		log.info("Transactions returned:" + transactions);
		return transactionMapper.mapToListTransactionDTO(transactions);
		
	}
	
	@PostMapping(value = "/transactionStatus")
	@Operation(description = "This method is used for querying the status of a movement")
	public TransactionDTO transactionStatus(@Valid @RequestBody TransactionStatusRequestDTO request) {
		var transaction = transactionStatusUseCase.getTransactionStatus(request);
		
		return transactionMapper.mapToTransactionDTO(transaction.orElseGet(() -> new Transaction()));
		
	}
	
	@GetMapping(value = "sayHello", consumes = "*/*")
	public String sayHello(@RequestParam String name) {
		log.info("EntityManager:" + entityManager);
		var account = new Account();
		log.info(emf.getPersistenceUnitUtil().getIdentifier(account) + "");
		var accountRef = entityManager.getReference(Account.class, 10L);
		
		log.info("Account initialized:" + emf.getPersistenceUnitUtil().isLoaded(accountRef));
		accountRef.getIban();
		log.info("Account initialized after invoking a method:" + emf.getPersistenceUnitUtil().isLoaded(accountRef));
		return "Hello " + name;
	}
	
}
