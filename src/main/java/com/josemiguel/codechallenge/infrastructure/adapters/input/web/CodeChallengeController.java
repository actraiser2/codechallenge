package com.josemiguel.codechallenge.infrastructure.adapters.input.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.domain.model.events.AccountCreatedEvent;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountListDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.ForecastDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.output.amqp.AmqpEventPub;
import com.josemiguel.codechallenge.infrastructure.adapters.output.feign.WeatherClientApi;
import com.josemiguel.codechallenge.infrastructure.config.props.KafkaConfigProperties;
import com.josemiguel.codechallenge.infrastructure.mappers.AccountMapper;
import com.josemiguel.codechallenge.infrastructure.mappers.TransactionMapper;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Tag(name = "Services available for the project code challenge")
@Slf4j
@CrossOrigin()
public class CodeChallengeController {

	private final CreateAccountUseCase createAccountUseCase;
	private final CreateTransactionUseCase createTransactionUseCase;
	private final SearchTransactionsUseCase searchTransactionsUseCase;
	private final TransactionStatusUseCase transactionStatusUseCase;
	private final MeterRegistry meterRegistry;
	private final Environment env;
	private final AccountRepository accountRepository;
	@PersistenceContext
	private final EntityManager entityManager;
	private final TransactionMapper transactionMapper;
	private final AccountMapper accountMapper;
	//private StreamBridge streamBridge;
	private final TransactionTemplate transactionTemplate;
	private final WeatherClientApi weatherClientApi;
	private final AmqpEventPub eventPub;

	
	
	@PostMapping("/accounts")
	@Operation(description = "This method allows you to insert a new account")
	@Counted(description = "Number of accounts created")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) throws IOException, InterruptedException, ExecutionException {
		log.info("Calling createAccount");
		var accountId = createAccountUseCase.createAccount(command);
		//producerTemplate.sendBody("direct:restCodechallenge", command);
		meterRegistry.counter("accounts", "app", env.getProperty("app.version")).increment();
		//rabbitTemplate.convertAndSend("accounts", command);
		
		
		/*var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		var account = Account.newBuilder().
			setIban(command.iban()).setCurrency("Eur").
			setTimestamp(dateFormatter.format(LocalDate.now())).
			setBalance(0d).
			setAccountId(accountId).
			setAccountName(command.accountName()).
			setAccountType(AccountType.CASH_ACCOUNT).
			setHolderName(command.holderName()).
			build();*/
		
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
	@Transactional
	//@Cacheable(cacheNames = "accountsCache")
	public AccountListDTO accounts(){
		
		var accounts = transactionTemplate.execute(status -> accountRepository.findTop10ByOrderByAccountIdDesc());
		
		log.info("Accessing all accounts:" + accounts);
		return AccountListDTO.builder()
				.accountList(accountMapper.toListAccountDTO(accounts)).build();
	}
	
	@GetMapping("/accounts/{accountId}")
	@Cacheable("accountCache")
	public AccountDTO getAccount(@PathVariable Long accountId) {
		return accountMapper.toAccountDTO(accountRepository.
			findById(accountId).orElseThrow());
	}
	
	@PostMapping("/transactions")
	@Operation(description = "This method allows you to insert a new movement")
	public ResponseEntity<Void> createTransaction(@RequestBody @Valid CreateTransactionCommand command) {
		createTransactionUseCase.createTransaction(command);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/transactions", consumes = "*/*")
	@Operation(description = "This method is used for querying movements using a couple filters")
	@Cacheable(value = "transactions", key = "#iban")
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
	public void transactionStatus(@Valid @RequestBody AccountDTO account) {
		Objects.requireNonNull(account.getBalance());
		Objects.requireNonNull(account.getAccountId());

		
		accountRepository.updateBalance(account.getAccountId(), account.getBalance());
		
	}
	
	@GetMapping(value = "sayHello", consumes = "*/*")
	//@Cacheable(cacheNames = "heyWorld")
	public CompletionStage<String> sayHello(@RequestParam String name) throws InterruptedException {
		
		return CompletableFuture.supplyAsync(() -> {
			eventPub.sendEventAccountCreated(AccountCreatedEvent.builder().
					balance(0d).accountId(1l).iban("es123456789").build());
			return "Hello " + name;
		});
		
	}
	
	@PostMapping("forecast")
	@TimeLimiter(name = "weatherResilience")
	@Retry(name = "weatherResilience")
	public CompletionStage<Map<String, Object>> forecast(@RequestBody ForecastDTO forecast) throws InterruptedException, ExecutionException {
		log.info("weatherClientApi:" + weatherClientApi);
		return CompletableFuture.supplyAsync(() -> weatherClientApi.forecast(forecast.getLongitude(), 
						forecast.getLatitude(), 
						"temperature_2m,wind_speed_10m",
						"temperature_2m,relative_humidity_2m,wind_speed_10m"));
	}
	
}
