package com.sample.app.lazarinrestful.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sample.app.lazarinrestful.util.BigDecimalCustomSerializer;

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
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticVO implements Serializable {

	private static final long serialVersionUID = 478985975044562L;

	@JsonSerialize(using = BigDecimalCustomSerializer.class)
	private BigDecimal sum;

	@JsonSerialize(using = BigDecimalCustomSerializer.class)
	private BigDecimal avg;

	@JsonSerialize(using = BigDecimalCustomSerializer.class)
	private BigDecimal max;

	@JsonSerialize(using = BigDecimalCustomSerializer.class)
	private BigDecimal min;

	private long count;

	{
		this.sum = new BigDecimal(0);
		this.avg = new BigDecimal(0);
		this.max = new BigDecimal(0);
		this.min = new BigDecimal(0);
	}

}