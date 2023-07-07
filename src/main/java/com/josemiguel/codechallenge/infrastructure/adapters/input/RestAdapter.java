package com.josemiguel.codechallenge.infrastructure.adapters.input;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.camel.ProducerTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josemiguel.codechallenge.application.ports.output.AccountRepositoryOutputPort;
import com.josemiguel.codechallenge.application.usecases.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.usecases.CreateTransactionUseCase;
import com.josemiguel.codechallenge.application.usecases.SearchTransactionsUseCase;
import com.josemiguel.codechallenge.application.usecases.TransactionStatusUseCase;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountListDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;
import com.josemiguel.codechallenge.infrastructure.config.props.KafkaConfigProperties;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1", consumes = "application/json", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Services available for the project code challenge")
@Slf4j
public class RestAdapter {

	private CreateAccountUseCase createAccountUseCase;
	private CreateTransactionUseCase createTransactionUseCase;
	private SearchTransactionsUseCase searchTransactionsUseCase;
	private TransactionStatusUseCase transactionStatusUseCase;
	private ProducerTemplate producerTemplate;
	private MeterRegistry meterRegistry;
	private Environment env;
	private AccountRepositoryOutputPort accountRepository;
	private List<KafkaConfigProperties> kafkaPropertiesList;
	
	@PostMapping("/accounts")
	@Operation(description = "This method allows you to insert a new account")
	@Counted(description = "Number of accounts created")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) {
		createAccountUseCase.createAccount(command);
		//producerTemplate.sendBody("direct:restCodechallenge", command);
		meterRegistry.counter("accounts", "app", env.getProperty("app.version")).increment();
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/accounts", consumes = "*/*")
	@Operation(description = "This method lists all the accounts")
	public ResponseEntity<AccountListDTO> createAcgetAccountscount() {
		log.info("Accessing all accounts");
		var accounts = AccountListDTO.builder().
				accountList(accountRepository.findAll()).build();
		log.info("Done !!!!:" + kafkaPropertiesList);
		return ResponseEntity.ok(accounts);
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
		
		return searchTransactionsUseCase.searchTransactions(iban, sortByAmount).
				stream().
				map(t -> {
					var transactionDTO = new TransactionDTO();
					transactionDTO.setAmount(t.getAmount());
					transactionDTO.setFee(t.getFee());
					transactionDTO.setDescription(t.getDescription());
					transactionDTO.setReference(t.getReference());
					transactionDTO.setDate(t.getDate());
					
					return transactionDTO;
				}).
				collect(Collectors.toList());
		
	}
	
	@PostMapping(value = "/transactionStatus")
	@Operation(description = "This method is used for querying the status of a movement")
	public TransactionDTO transactionStatus(@Valid @RequestBody TransactionStatusRequestDTO request) {
		var transaction = transactionStatusUseCase.getTransactionStatus(request);
		var transactionStatus = transactionStatusUseCase.applyBusinessRule(transaction, request.getChannel());
		
		return transaction.
			map(t -> {
				var transactionDTO = new TransactionDTO();
				transactionDTO.setAmount(t.getAmount());
				transactionDTO.setFee(t.getFee());
				transactionDTO.setDescription(t.getDescription());
				transactionDTO.setReference(t.getReference());
				transactionDTO.setDate(t.getDate());
				transactionDTO.setStatus(transactionStatus);
				
				return transactionDTO;
			}).orElseGet(() -> {
				var transactionDTO = new TransactionDTO();
				transactionDTO.setStatus(transactionStatus);
				return transactionDTO;
			});
		
	}
	
	@GetMapping(value = "sayHello", consumes = "*/*")
	public String sayHello(@RequestParam String name) {
		return "Hello " + name;
	}
	
}
