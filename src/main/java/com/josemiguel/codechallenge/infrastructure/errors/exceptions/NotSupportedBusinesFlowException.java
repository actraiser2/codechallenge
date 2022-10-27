package com.josemiguel.codechallenge.infrastructure.errors.exceptions;

public class NotSupportedBusinesFlowException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	public NotSupportedBusinesFlowException(String message) {
		super(message);
	}
}
