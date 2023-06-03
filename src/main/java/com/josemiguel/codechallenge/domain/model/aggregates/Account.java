package com.josemiguel.codechallenge.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.AccountBalanceBelowZeroException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "TB_ACCOUNTS")
@Data
@NoArgsConstructor
@Slf4j
@AllArgsConstructor
@Builder
public class Account {

	@Id
	@GeneratedValue
	@Column(name = "ACCOUNT_ID")
	@Nullable
	private Long accountId;
	
	@Column(name = "BALANCE")
	private BigDecimal balance;
	
	@Column(name = "ACCOUNT_NAME")
	private String accountName;
	
	@JsonIgnore
	@Column(name = "IBAN")
	private String iban;
	
	@Column(name = "CREATION_DATE")
	@CreationTimestamp
	@Nullable
	private LocalDateTime timestamp;
	
	@Version
	private Long version;
	
	public Account(CreateAccountCommand command) {
		this.accountName = command.getAccountName();
		this.iban = command.getIban();
		this.balance = new BigDecimal(0);
	}
	
	public Transaction addTransaction(CreateTransactionCommand command) {
		//Validating if balance drops below 0
		BigDecimal netAmount = command.getFee() != null ? 
				command.getAmount().subtract(command.getFee()) : command.getAmount();
		
		log.info(netAmount + "");
		log.info("Account balance:" + this.getBalance());
		if (this.balance.add(netAmount).doubleValue() < 0){
			throw new AccountBalanceBelowZeroException("The account balance can not drop below 0");
		}
		else {
			//The transaction is valid
			this.balance = balance.add(netAmount);
			
			var transaction = Transaction.builder().
			account(this).
			amount(command.getAmount().doubleValue()).
			fee(command.getFee()).
			date(command.getOperationDate()).
			reference(command.getReference() != null ? command.getReference() : UUID.randomUUID().toString()).
			description(command.getDescription()).build();
			
			return transaction;
		}
	}
	
	
	
}
