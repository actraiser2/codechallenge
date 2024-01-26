package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class AccountListDTO {

	@With
	private List<AccountDTO> accountList;
}
