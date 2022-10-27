package com.josemiguel.codechallenge.infrastructure.errors.exceptions;

public class AccountBalanceBelowZeroException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AccountBalanceBelowZeroException(String message) {
		super(message);
	}
}
