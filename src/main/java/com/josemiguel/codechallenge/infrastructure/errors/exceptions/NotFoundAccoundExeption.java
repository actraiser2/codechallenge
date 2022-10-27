package com.josemiguel.codechallenge.infrastructure.errors.exceptions;

public class NotFoundAccoundExeption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundAccoundExeption(String message) {
		super(message);
	}

}
