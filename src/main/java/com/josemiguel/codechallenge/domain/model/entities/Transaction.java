package com.josemiguel.codechallenge.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRANSACTIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_seq")
	@SequenceGenerator(sequenceName = "transactions_seq", name = "transactions_seq")
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
