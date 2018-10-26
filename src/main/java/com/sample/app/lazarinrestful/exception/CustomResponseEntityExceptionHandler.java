package com.sample.app.lazarinrestful.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lazarin, Carlos
 *
 */
@Slf4j
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
		logError(ex);
		return new ResponseEntity<Object>(
				new ExceptionalResponse(new Date(),  ex.getLocalizedMessage(), request.getDescription(false)),
				HttpStatus.BAD_REQUEST);

	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		
		if(ex.contains(java.time.format.DateTimeParseException.class)) {
			return new ResponseEntity<Object>(
					new ExceptionalResponse(new Date(), "Invalid timestamp provided in the payload", request.getDescription(false)),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		if(ex.contains(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)) {
			return new ResponseEntity<Object>(
					new ExceptionalResponse(new Date(), "Invalid data provided in the payload", request.getDescription(false)),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		return super.handleHttpMessageNotReadable(ex, headers, status, request);
	}

	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		logError(ex);
		return new ResponseEntity<Object>(
				new ExceptionalResponse(new Date(), "Provided payload does not matched expected one", ex.getLocalizedMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logError(ex);
		return new ResponseEntity<Object>(
				new ExceptionalResponse(new Date(), "Validation Failed", ex.getLocalizedMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logError(ex);
		return new ResponseEntity<Object>(
				new ExceptionalResponse(new Date(), "Provided payload is missing parameters", ex.getLocalizedMessage()),
				HttpStatus.BAD_REQUEST);
	}
	
	private void logError(Exception ex) {
		log.error("Handling entity reponse exception: "+  ex.getLocalizedMessage());
	}
}