package com.sample.app.lazarinrestful.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.sample.app.lazarinrestful.service.TransactionManagerService;
import com.sample.app.lazarinrestful.vo.StatisticVO;
import com.sample.app.lazarinrestful.vo.TransactionVO;


@RunWith(MockitoJUnitRunner.class)
public class TransactionRestControllerTest {
	
	@Mock
    private TransactionManagerService transactionManagerService;
	
	@InjectMocks
	private TransactionRestController transactionRestController = new TransactionRestController();
	
	@Mock
	private UriComponents uriComponents;
	
	@Test
	public void testCreateNewTransaction() throws Exception {
		TransactionVO stub = new TransactionVO();
		stub.setAmount(new BigDecimal("1000.00"));
		stub.setTimestamp(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC")));
		when(transactionManagerService.verifyIfTransactionIsNotTooOld(stub)).thenReturn(true);
		ResponseEntity<Void> resp = transactionRestController.createNewTransaction(stub);
		assertNotNull(resp);
		assertEquals(HttpStatus.CREATED, resp.getStatusCode());
		verify(transactionManagerService, times(1)).createNewTransaction(stub);
		verify(transactionManagerService, times(1)).verifyIfTransactionIsNotTooOld(stub);
	}
	
	@Test
	public void testCreateNewTransaction_tooOld() throws Exception {
		TransactionVO stub = new TransactionVO();
		stub.setAmount(new BigDecimal("1000.00"));
		ZonedDateTime nowZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));
		nowZonedDateTime.minus(Duration.ofMinutes(4));
		stub.setTimestamp(nowZonedDateTime);
		ResponseEntity<Void> resp = transactionRestController.createNewTransaction(stub);
		assertNotNull(resp);
		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
	}
	
	@Test
	public void testCreateNewTransaction_invalidTimeStamp() throws Exception {
		TransactionVO stub = new TransactionVO();
		stub.setAmount(new BigDecimal("1000.00"));
		ZonedDateTime nowZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));
		stub.setTimestamp(nowZonedDateTime.plus(Duration.ofDays(4)));
		ResponseEntity<Void> resp = transactionRestController.createNewTransaction(stub);
		assertNotNull(resp);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
	}
	
	@Test
	public void testGetStatistics() throws Exception {
		ResponseEntity<StatisticVO> resp = transactionRestController.getStatistics();
		assertNotNull(resp);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		verify(transactionManagerService, times(1)).provideTransactionsStatisticData();
	}
	
	@Test
	public void testDeleteTransactions() throws Exception {
		ResponseEntity<Void> resp = transactionRestController.deleteTransactions();
		assertNotNull(resp);
		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
		verify(transactionManagerService, times(1)).deleteAllTransactions();
	}

}