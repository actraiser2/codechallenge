package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.josemiguel.codechallenge.domain.model.aggregates.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountListDTO {

	@JsonProperty("account_list")
	private List<Account> accountList;
}
