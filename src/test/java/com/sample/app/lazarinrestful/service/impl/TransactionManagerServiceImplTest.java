package com.sample.app.lazarinrestful.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sample.app.lazarinrestful.service.TransactionManagerService;
import com.sample.app.lazarinrestful.vo.StatisticVO;
import com.sample.app.lazarinrestful.vo.TransactionVO;

@RunWith(MockitoJUnitRunner.class)
public class TransactionManagerServiceImplTest {
	
	@InjectMocks
	private TransactionManagerService transactionManagerServiceImpl = new TransactionManagerServiceImpl();
	
	@Test
	public void testCreateNewTransaction() {
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("1000.00"),nowZonedDateTime));
	}
	
	private ZonedDateTime nowZonedDateTime;
	
	@Before
	public void setup() {
		nowZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));
	}
	
	private TransactionVO getMockedTransactionVO(BigDecimal amount, ZonedDateTime timestamp) {
		TransactionVO stub = new TransactionVO();
		stub.setAmount(amount);
		stub.setTimestamp(timestamp);
		return stub;
	}

	@Test
	public void testProvideTransactionsStatisticData() {
		transactionManagerServiceImpl.deleteAllTransactions();
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("25.00"),nowZonedDateTime));
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("1000.00"),nowZonedDateTime));
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("300.00"),nowZonedDateTime));
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("20.00"),nowZonedDateTime));
		transactionManagerServiceImpl.createNewTransaction(getMockedTransactionVO(new BigDecimal("10.00"),nowZonedDateTime));
		StatisticVO resp = transactionManagerServiceImpl.provideTransactionsStatisticData();
		assertNotNull(resp);
		assertNotNull(resp.getAvg());
		assertNotNull(resp.getCount());
		assertNotNull(resp.getMax());
		assertNotNull(resp.getMin());
		assertNotNull(resp.getSum());
		assertEquals(new BigDecimal("1355.00"),resp.getSum());
		assertEquals(new BigDecimal("1000.00"),resp.getMax());
		assertEquals(5l,resp.getCount());
		assertEquals(new BigDecimal("10.00"),resp.getMin());
		assertEquals(new BigDecimal("271.00"),resp.getAvg());
	}
	
	@Test
	public void testVerifyIfTransactionIsNotTooOld() {
		boolean resp = transactionManagerServiceImpl.verifyIfTransactionIsNotTooOld(getMockedTransactionVO(new BigDecimal("1000.00"),nowZonedDateTime));
		assertTrue(resp);
	}
	
	@Test
	public void testVerifyIfTransactionIsNotTooOld_oldDate() {
		ZonedDateTime nowZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));
		boolean resp = transactionManagerServiceImpl.verifyIfTransactionIsNotTooOld(
				getMockedTransactionVO(new BigDecimal("1000.00"),nowZonedDateTime.minus(Duration.ofMinutes(4))));
		assertFalse(resp);
	}
	
	@Test
	public void testDeleteAllTransactions() {
		transactionManagerServiceImpl.deleteAllTransactions();
		StatisticVO resp = transactionManagerServiceImpl.provideTransactionsStatisticData();
		assertNotNull(resp);
		assertEquals(new BigDecimal(0),resp.getSum());
	}	

}