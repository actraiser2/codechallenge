package com.josemiguel.codechallenge.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.TransactionDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
	
	public TransactionDTO mapToTransactionDTO(Transaction transaction);
	
	public List<TransactionDTO> mapToListTransactionDTO(List<Transaction> transactions);
}
