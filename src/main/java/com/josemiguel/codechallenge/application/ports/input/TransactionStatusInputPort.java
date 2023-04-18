package com.josemiguel.codechallenge.application.ports.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.josemiguel.codechallenge.application.ports.output.TransactionRepositoryOutputPort;
import com.josemiguel.codechallenge.application.usecases.TransactionStatusUseCase;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.domain.model.valueobjects.Channel;
import com.josemiguel.codechallenge.domain.model.valueobjects.TransactionStatus;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionStatusRequestDTO;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.NotSupportedBusinesFlowException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionStatusInputPort implements TransactionStatusUseCase {
	private TransactionRepositoryOutputPort transactionRepository;
	
	
	@Override
	public Optional<Transaction> getTransactionStatus(TransactionStatusRequestDTO request) {
		// TODO Auto-generated method stub
		Optional<Transaction> transaction = transactionRepository.findByReference(request.getReference());
		
		
		return transaction;
	}


	@Override
	public TransactionStatus applyBusinessRule(Optional<Transaction> transaction, Channel channel) {
		// TODO Auto-generated method stub
		TransactionStatus status = null;
		
		if (transaction.isEmpty()) {
			/*
			 * Given: A transaction that is not stored in our system
				When: I check the status from any channel
				Then: The system returns the status 'INVALID'
			 */
			status = TransactionStatus.INVALID;
		}
		else {
			/*Given: A transaction that is stored in our system
			When: I check the status from CLIENT or ATM channel
			And the transaction date is before today
			Then: The system returns the status 'SETTLED'
			And the amount substracting the fee*/
			
			if (channel != null && (channel == Channel.CLIENT || channel == Channel.ATM) && 
					transaction.get().getDate().isBefore(ZonedDateTime.now())) {
				status = TransactionStatus.SETTLED;
				Double newAmount = transaction.get().getAmount() - transaction.get().getFee().doubleValue();
				transaction.get().setAmount(newAmount.doubleValue());
				transaction.get().setFee(null);
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from INTERNAL channel
			And the transaction date is before today
			Then: The system returns the status 'SETTLED'
			And the amount
			And the fee*/
			else if (channel != null && channel == Channel.INTERNAL && 
					transaction.get().getDate().isBefore(ZonedDateTime.now())) {
				status = TransactionStatus.SETTLED;
				
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from CLIENT or ATM channel
			And the transaction date is equals to today
			Then: The system returns the status 'PENDING'
			And the amount substracting the fee*/
			
			else if (channel != null && (channel == Channel.CLIENT || channel == Channel.ATM) && 
					transaction.get().getDate().isEqual(ZonedDateTime.now())) {
				status = TransactionStatus.PENDING;
				Double newAmount = transaction.get().getAmount() - transaction.get().getFee().doubleValue();
				transaction.get().setAmount(newAmount.doubleValue());
				transaction.get().setFee(null);
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from INTERNAL channel
			And the transaction date is equals to today
			Then: The system returns the status 'PENDING'
			And the amount
			And the fee*/
			else if(channel != null && channel == Channel.INTERNAL && 
					transaction.get().getDate().isEqual(ZonedDateTime.now())) {
				status = TransactionStatus.PENDING;
				
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from CLIENT channel
			And the transaction date is greater than today
			Then: The system returns the status 'FUTURE'
			And the amount substracting the fee*/
			else if (channel != null && channel == Channel.CLIENT && 
					transaction.get().getDate().isAfter(ZonedDateTime.now())) {
				status = TransactionStatus.FUTURE;
				Double newAmount = transaction.get().getAmount() - transaction.get().getFee().doubleValue();
				transaction.get().setAmount(newAmount);
				transaction.get().setFee(null);
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from ATM channel
			And the transaction date is greater than today
			Then: The system returns the status 'PENDING'
			And the amount substracting the fee*/
			else if (channel != null && channel == Channel.ATM && 
					transaction.get().getDate().isAfter(ZonedDateTime.now())) {
				status = TransactionStatus.PENDING;
				Double newAmount = transaction.get().getAmount() - transaction.get().getFee().doubleValue();
				transaction.get().setAmount(newAmount);
				transaction.get().setFee(null);
			}
			
			/*Given: A transaction that is stored in our system
			When: I check the status from INTERNAL channel
			And the transaction date is greater than today
			Then: The system returns the status 'FUTURE'
			And the amount
			And the fee*/
			else if (channel != null && channel == Channel.INTERNAL && 
					transaction.get().getDate().isAfter(ZonedDateTime.now())) {
				status = TransactionStatus.FUTURE;
				
			}
			
			else {
				throw new NotSupportedBusinesFlowException("Flow not supported");
			}
		}
		
		return status;
	}

}
