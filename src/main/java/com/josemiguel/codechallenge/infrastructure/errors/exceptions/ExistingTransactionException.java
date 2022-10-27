package com.josemiguel.codechallenge.infrastructure.errors.exceptions;

public class ExistingTransactionException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ExistingTransactionException(String message) {
		super(message);
	}
}
