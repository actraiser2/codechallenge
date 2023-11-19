package com.josemiguel.codechallenge.infrastructure.errors.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.josemiguel.codechallenge.infrastructure.errors.dto.ErrorDTO;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.AccountBalanceBelowZeroException;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.ExistingTransactionException;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.NotFoundAccoundExeption;
import com.josemiguel.codechallenge.infrastructure.errors.exceptions.NotSupportedBusinesFlowException;

import lombok.extern.slf4j.Slf4j;

//@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

	/*@ExceptionHandler(NotFoundAccoundExeption.class)
	public ResponseEntity<ErrorDTO> handleNotFoundAccoundExeption(NotFoundAccoundExeption ex) {
		var error = new ErrorDTO();
		error.setDescription(ex.getMessage());
		return ResponseEntity.ok().body(error);
	}*/
	
	@ExceptionHandler(ExistingTransactionException.class)
	public ResponseEntity<ErrorDTO> handleExistingTransactionException(ExistingTransactionException ex) {
		var error = new ErrorDTO();
		error.setDescription(ex.getMessage());
		return ResponseEntity.ok().body(error);
	}
	
	@ExceptionHandler(AccountBalanceBelowZeroException.class)
	public ResponseEntity<ErrorDTO> handleAccountBalanceBelowZeroException(AccountBalanceBelowZeroException ex) {
		var error = new ErrorDTO();
		error.setDescription(ex.getMessage());
		return ResponseEntity.ok().body(error);
	}
	
	@ExceptionHandler(NotSupportedBusinesFlowException.class)
	public ResponseEntity<ErrorDTO> handleNotSupportedBusinesFlowException(NotSupportedBusinesFlowException ex) {
		var error = new ErrorDTO();
		error.setDescription(ex.getMessage());
		return ResponseEntity.ok().body(error);
	}
}
