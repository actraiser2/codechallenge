package com.josemiguel.codechallenge.domain.model.events;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AccountCreatedEvent {

	private Long accountId;
	private String iban;
	private Double balance;
	private String fileName;
}
