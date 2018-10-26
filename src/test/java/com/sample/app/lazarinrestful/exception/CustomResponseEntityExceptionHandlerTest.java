package com.sample.app.lazarinrestful.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RunWith(MockitoJUnitRunner.class)
public class CustomResponseEntityExceptionHandlerTest {

	@Mock
	private Exception exception;
	
	@Mock
	private HttpMessageNotReadableException httpMessageNotReadableException;
	
	private HttpHeaders httpHeaders = new HttpHeaders() {
		
	};
	
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	
	@Mock
	private WebRequest webRequest;
	
	@Mock
	private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;
	
	@Mock
	private MissingServletRequestParameterException missingServletRequestParameterException;
	
	@Mock
	private MethodArgumentNotValidException methodArgumentNotValidException;
	
	@InjectMocks
	private CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler = new CustomResponseEntityExceptionHandler();
	
	@Before
	public void setup() {
		when(exception.getLocalizedMessage()).thenReturn("Sample java.lang.Exception localized message");
		when(webRequest.getDescription(false)).thenReturn("http://sample-host/samplerequest");
		when(methodArgumentTypeMismatchException.getLocalizedMessage()).thenReturn("Sample java.lang.Exception localized message");
		when(missingServletRequestParameterException.getLocalizedMessage()).thenReturn("Sample java.lang.Exception localized message");
		when(methodArgumentNotValidException.getLocalizedMessage()).thenReturn("Sample java.lang.Exception localized message");
	}
	
	@Test
	public void testHandleAllExceptions() throws Exception {
		ResponseEntity<Object> resp = customResponseEntityExceptionHandler.handleAllExceptions(exception, webRequest);
		assertNotNull(resp.getBody());
		ExceptionalResponse exceptionalResponse = ((ExceptionalResponse)resp.getBody());
		assertEquals("Sample java.lang.Exception localized message", exceptionalResponse.getMessage());
		assertEquals("http://sample-host/samplerequest", exceptionalResponse.getDetails());
		assertNotNull(exceptionalResponse.getTimestamp());
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertNotNull(resp);
	}
	
	@Test
	public void testHandleHttpMessageNotReadable() {
		ResponseEntity<Object> resp = customResponseEntityExceptionHandler.
				handleHttpMessageNotReadable(httpMessageNotReadableException,
				httpHeaders, httpStatus, webRequest);
		assertNotNull(resp);
	}
	
	@Test
	public void testHandleMethodArgumentTypeMismatch() {
		ResponseEntity<Object> resp = customResponseEntityExceptionHandler.handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, webRequest);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		ExceptionalResponse exceptionalResponse = ((ExceptionalResponse)resp.getBody());
		assertEquals("Provided payload does not matched expected one",exceptionalResponse.getMessage());
		assertEquals("Sample java.lang.Exception localized message", exceptionalResponse.getDetails());
		assertNotNull(exceptionalResponse.getTimestamp());
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void testHandleMethodArgumentNotValid() {
		ResponseEntity<Object> resp = customResponseEntityExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders,httpStatus, webRequest);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		ExceptionalResponse exceptionalResponse = ((ExceptionalResponse)resp.getBody());
		assertEquals("Validation Failed",exceptionalResponse.getMessage());
		assertNotNull(exceptionalResponse.getTimestamp());
		assertEquals("Sample java.lang.Exception localized message", exceptionalResponse.getDetails());
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void testHandleMissingServletRequestParameter() {
		ResponseEntity<Object> resp = customResponseEntityExceptionHandler.handleMissingServletRequestParameter(missingServletRequestParameterException, httpHeaders,httpStatus, webRequest);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		ExceptionalResponse exceptionalResponse = ((ExceptionalResponse)resp.getBody());
		assertEquals("Provided payload is missing parameters",exceptionalResponse.getMessage());
		assertEquals("Sample java.lang.Exception localized message", exceptionalResponse.getDetails());
		assertNotNull(exceptionalResponse.getTimestamp());
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
}
