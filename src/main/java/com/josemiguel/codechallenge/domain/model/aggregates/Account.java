package com.josemiguel.codechallenge.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.commands.CreateTransactionCommand;
import com.josemiguel.codechallenge.domain.model.entities.AuditingEntity;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.AccountBalanceBelowZeroException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "ACCOUNTS", indexes = @Index(columnList = "iban"))
@Immutable
@Data()
@NoArgsConstructor
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class Account extends AuditingEntity	{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
	@SequenceGenerator(name="accounts_seq",sequenceName="accounts_seq", allocationSize=1, initialValue = 10)
	@Column(name = "ACCOUNT_ID")
	private Long accountId;
	
	@Column(name = "BALANCE")
	@Access(AccessType.PROPERTY)
	@Getter(AccessLevel.NONE)
	private BigDecimal balance;
	
	@Column(name = "ACCOUNT_NAME")
	@Basic(optional = false)
	private String accountName;
	
	@Column(name = "IBAN")
	private String iban;
	
	@Column(name = "CREATION_DATE")
	@CreationTimestamp
	private LocalDateTime timestamp;
	
	@Column(name = "CURRENCY")
	//@ColumnDefault(value = "EUR")
	private String currency;
	
	@Version
	@Column(name = "VERSION")
	private Long version;
	
	public Account(CreateAccountCommand command) {
		this.accountName = command.accountName();
		this.iban = command.iban();
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
			transaction.setAmount(command.getAmount());
			transaction.setFee(command.getFee());
			transaction.setDate(command.getOperationDate());
			transaction.setReference(command.getReference() != null ? command.getReference() : UUID.randomUUID().toString());
			transaction.setDescription(command.getDescription());
			
			return transaction;
		}
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
	
	
}
