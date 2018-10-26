package com.sample.app.lazarinrestful.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lazarin, Carlos
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class ExceptionalResponse {

	private Date timestamp;
	private String message;
	private String details;
	
}