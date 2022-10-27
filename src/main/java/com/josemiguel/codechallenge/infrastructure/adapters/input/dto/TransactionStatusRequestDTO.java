package com.josemiguel.codechallenge.infrastructure.adapters.input.dto;

import javax.validation.constraints.NotEmpty;

import com.josemiguel.codechallenge.domain.model.valueobjects.Channel;

import lombok.Data;

@Data
public class TransactionStatusRequestDTO {

	@NotEmpty  private String reference;
	private Channel channel;
}
