package com.josemiguel.codechallenge.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;

import lombok.Data;

@Entity
@Table(name = "TB_TRANSACTIONS")
@Data
public class Transaction {

	@Id
	@GeneratedValue
	private Long transactionId;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "OPERATION_DATE")
	private LocalDateTime date;
	
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	
	@Column(name = "FEE")
	private BigDecimal fee;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;
	
	@Transient
	private TransactionStatus transactionStatus;
	
	
}
