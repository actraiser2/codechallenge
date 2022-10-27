package com.josemiguel.codechallenge.infrastructure.adapters.input;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1", consumes = "application/json", produces = "application/json")
@AllArgsConstructor
public class RestAdapter {

	private CreateAccountUseCase createAccountUseCase;
	private CreateTransactionUseCase createTransactionUseCase;
	private SearchTransactionsUseCase searchTransactionsUseCase;
	private TransactionStatusUseCase transactionStatusUseCase;
	
	@PostMapping("/account")
	public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountCommand command) {
		createAccountUseCase.createAccount(command);
		return ResponseEntity.status(201).build();
	}
	
	@PostMapping("/transaction")
	public ResponseEntity<Void> createTransaction(@RequestBody @Valid CreateTransactionCommand command) {
		createTransactionUseCase.createTransaction(command);
		return ResponseEntity.status(201).build();
	}
	
	@GetMapping(value = "/transactions", consumes = "*/*")
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
	
}
