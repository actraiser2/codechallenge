package com.josemiguel.codechallenge.infrastructure.adapters.input;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.camel.ProducerTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josemiguel.codechallenge.application.usecases.CreateAccountUseCase;
import com.josemiguel.codechallenge.application.usecases.CreateTransactionUseCase;
import com.josemiguel.codechallenge.application.usecases.SearchTransactionsUseCase;
import com.josemiguel.codechallenge.application.usecases.TransactionStatusUseCase;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v7", consumes = "application/json", produces = "application/json")
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
	private JobLauncher jobLauncher;
	@Qualifier("Job1")
	private Job job1;
	private JobExplorer jobExplorer;
	
	@PostMapping("/accounts")
	@Operation(description = "This method allows you to insert a new account")
	@Counted(description = "Number of accounts created")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) {
		createAccountUseCase.createAccount(command);
		producerTemplate.sendBody("direct:restCodechallenge", command);
		meterRegistry.counter("accounts", "app", env.getProperty("app.version")).increment();
		return ResponseEntity.status(201).build();
	}
	
	@PostMapping("/transactions")
	@Operation(description = "This method allows you to insert a new movement")
	public ResponseEntity<Void> createTransaction(@RequestBody @Valid CreateTransactionCommand command) {
		createTransactionUseCase.createTransaction(command);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/transactions", consumes = "*/*")
	@Operation(description = "This method is used for querying movements using a couple filters")
	public Map<String, Set<BigDecimal>> searchTransactions(@RequestParam String iban, @RequestParam int sortByAmount) {
		
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
				collect(Collectors.groupingBy(TransactionDTO::getReference,
						Collectors.mapping(TransactionDTO::getFee, Collectors.toSet())));
		
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
	
	@GetMapping("/ping")
	public void ping() throws Exception {
		log.info("Jobs running (1):"+ jobExplorer.findRunningJobExecutions("job1"));
		
		Path base = Paths.get("/tmp");
		
		Instant yesterday = Instant.now().minusSeconds(86400);
		Stream<Path> stream = Files.find(base, 1, (p, a) -> {
			return a.isRegularFile() && a.lastModifiedTime().toInstant().isAfter(yesterday);
		}).sorted(Comparator.reverseOrder());
		
		stream.
			filter(f -> f.toString().contains(".log")).
			forEach(f -> {
				try {
					jobLauncher.run(job1, new JobParametersBuilder().
							addDate("timestamp", new Date()).
							addString("filePath", f.toString()).
							toJobParameters());
				}
				catch(Exception ex) {
					log.error("Error:", ex);
				}
			});
		
		log.info("Jobs running (2):"+ jobExplorer.findRunningJobExecutions("job1"));
	}
	
	@GetMapping("/ip")
	public String ip(HttpServletRequest request) {
		return request.getLocalAddr();
	}
	
}
