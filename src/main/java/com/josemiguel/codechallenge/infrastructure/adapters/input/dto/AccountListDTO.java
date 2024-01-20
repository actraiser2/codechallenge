package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.util.List;

import lombok.Value;
import lombok.With;

@Value
public class AccountListDTO {

	@With
	private List<AccountDTO> accountList;
}
