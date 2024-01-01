package com.josemiguel.codechallenge.infrastructure.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.EqualsAndHashCode;
import lombok.Value;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@Value
@EqualsAndHashCode(callSuper=false)
public class NotFoundAccoundExeption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long accountId;
	
	public NotFoundAccoundExeption(Long accountId) {
		super("Account not found:" + accountId);
		this.accountId = accountId;
		
	}
	
	public NotFoundAccoundExeption(String message) {
		super(message);
		accountId = 0l;
	}

}
