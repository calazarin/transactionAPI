package com.sample.app.lazarinrestful.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Lazarin, Carlos
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVO implements Serializable{

	private static final long serialVersionUID = 4789859045437504662L;

	@NotNull(message="Transaction amount is mandatory")
	private  BigDecimal amount;
	
	@NotNull(message="Transaction timestamp is mandatory")
	private ZonedDateTime timestamp;
	
}