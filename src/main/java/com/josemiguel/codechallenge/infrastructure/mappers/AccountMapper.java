package com.josemiguel.codechallenge.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import com.josemiguel.codechallenge.domain.model.aggregates.Account;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountDTO;

@Mapper(componentModel = ComponentModel.SPRING )
public interface AccountMapper {

	public List<AccountDTO> toListAccountDTO(List<Account> accounts);
	
}
