package com.josemiguel.codechallenge.infrastructure.adapters.input.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.josemiguel.codechallenge.application.ports.input.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.ports.input.CreateTransactionUseCase;
import com.josemiguel.codechallenge.application.ports.input.SearchTransactionsUseCase;
import com.josemiguel.codechallenge.application.ports.input.TransactionStatusUseCase;
import com.josemiguel.codechallenge.application.ports.output.AccountRepository;
import com.josemiguel.codechallenge.domain.Account;
import com.josemiguel.codechallenge.domain.AccountType;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.domain.model.events.Event;
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
	private AccountRepository accountRepository;
	private List<KafkaConfigProperties> kafkaPropertiesList;
	private EntityManagerFactory emf;
	@PersistenceContext
	private final EntityManager entityManager;
	private RabbitTemplate rabbitTemplate;
	private TransactionMapper transactionMapper;
	private AccountMapper accountMapper;
	//private StreamBridge streamBridge;
	private TransactionTemplate transactionTemplate;
	
	@PostMapping("/accounts")
	@Operation(description = "This method allows you to insert a new account")
	@Counted(description = "Number of accounts created")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) throws IOException, InterruptedException, ExecutionException {
		log.info("Calling createAccount");
		var accountId = createAccountUseCase.createAccount(command);
		//producerTemplate.sendBody("direct:restCodechallenge", command);
		meterRegistry.counter("accounts", "app", env.getProperty("app.version")).increment();
		//rabbitTemplate.convertAndSend("accounts", command);
		
		
		var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		var account = Account.newBuilder().
			setIban(command.iban()).setCurrency("Eur").
			setTimestamp(dateFormatter.format(LocalDate.now())).
			setBalance(0d).
			setAccountId(accountId).
			setAccountName(command.accountName()).
			setAccountType(AccountType.CASH_ACCOUNT).
			setHolderName(command.holderName()).
			build();
		
		/*DatumWriter<Account> userDatumWriter = new SpecificDatumWriter<>(Account.class);
		var out = new ByteArrayOutputStream();
		var binaryEncoder = EncoderFactory.get().binaryEncoder(out, null);
		
		userDatumWriter.write(account, binaryEncoder);
		binaryEncoder.flush();
		
		log.info("Sent {} bytes", out.toByteArray().length);*/
		//streamBridge.send("accounts-out-0", account);
					
	
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/accounts", consumes = "*/*")
	@Operation(description = "This method lists all the accounts")
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<AccountDTO> accounts(JwtAuthenticationToken  jwtToken) throws InterruptedException {
		
		var accounts = transactionTemplate.execute(status -> accountRepository.findTop10ByOrderByAccountIdDesc());
		
		//log.info("Accessing all accounts:" + accounts);
		//return ResponseEntity.of(Optional.ofNullable(accountMapper.toListAccountDTO(accounts)));
		try(var accountsStream = accountRepository.findTop20ByOrderByAccountIdDesc().stream()){
			return accountMapper.toListAccountDTO(accountsStream.toList());
		}
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
	
	@PatchMapping(value = "/accounts/balance")
	@Operation(description = "This method is used for updating the balance")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Transactional
	public void transactionStatus(@RequestBody AccountDTO account) {
		Objects.requireNonNull(account.getBalance());
		Objects.requireNonNull(account.getAccountId());
		
		accountRepository.updateBalance(account.getAccountId(), account.getBalance());
		
	}
	
	@GetMapping(value = "sayHello", consumes = "*/*")
	public CompletionStage<String> sayHello(@RequestParam String name) throws InterruptedException {
		Thread.sleep(5000);
		
		return CompletableFuture.supplyAsync(() -> "Hello " + name);
	}
	
	private void sendMessage(String bindingName, Event<Long, CreateAccountCommand> event) {
		var message = MessageBuilder.withPayload(event).
				setHeader("partitionKey", event.getKey()).build();
		//streamBridge.send(bindingName, message);
		
		
	}
	
}
