package com.josemiguel.codechallenge.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.AccountBalanceBelowZeroException;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "TB_ACCOUNTS")
@Data
@NoArgsConstructor
@Slf4j
public class Account {

	@Id
	@GeneratedValue
	@Column(name = "ACCOUNT_ID")
	private Long accountId;
	
	@Column(name = "BALANCE")
	private BigDecimal balance;
	
	@Column(name = "ACCOUNT_NAME")
	private String accountName;
	
	@Column(name = "IBAN")
	private String iban;
	
	@Column(name = "CREATION_DATE")
	@CreationTimestamp
	private LocalDateTime timestamp;
	
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
			
			var transaction = new Transaction();
			transaction.setAccount(this);
			transaction.setAmount(command.getAmount().doubleValue());
			transaction.setFee(command.getFee());
			transaction.setDate(command.getOperationDate());
			transaction.setReference(command.getReference() != null ? command.getReference() : UUID.randomUUID().toString());
			transaction.setDescription(command.getDescription());
			
			return transaction;
		}
	}
	
	
	
}
