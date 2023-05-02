package com.josemiguel.codechallenge.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_TRANSACTIONS")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue
	private Long transactionId;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "OPERATION_DATE")
	private ZonedDateTime date;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "FEE")
	private BigDecimal fee;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;
	
	@Transient
	private TransactionStatus transactionStatus;
	
	private String password = "xpPXhP0NNhHltA9menSRLIc200";
	
	
}
