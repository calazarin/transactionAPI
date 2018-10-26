package com.sample.app.lazarinrestful.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Class used to have a proper representation of BigDecimal values in the output (as JSON format)
 * 
 * @author Lazarin, Carlos
 *
 */
public class BigDecimalCustomSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		 gen.writeString(String.format(Locale.US,"%.2f", value));
	}   
	
}