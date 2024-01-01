package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.util.List;

import lombok.Value;

@Value
public class AccountListDTO {

	private List<AccountDTO> accountList;
}
