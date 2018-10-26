package com.sample.app.lazarinrestful.service;

import com.sample.app.lazarinrestful.vo.StatisticVO;
import com.sample.app.lazarinrestful.vo.TransactionVO;

/**
 * 
 * @author Lazarin, Carlos
 *
 */
public interface TransactionManagerService {
	
	/**
	 * Creates a new transaction
	 * 
	 * @param transactionVO {@link TransactionVO}
	 */
	void createNewTransaction(TransactionVO transactionVO);
	
	/**
	 * Verifies if given transaction is not older than 60 seconds
	 * 
	 * @param transactionVO
	 * @return true/false
	 */
	boolean verifyIfTransactionIsNotTooOld(TransactionVO transactionVO);
	
	/**
	 * Deletes all transactions
	 */
	void deleteAllTransactions();
	
	/**
	 * PRovides transactions statistic data
	 * 
	 * @return {@link StatisticVO}
	 */
	StatisticVO provideTransactionsStatisticData() ;
	
}