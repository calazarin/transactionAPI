package com.sample.app.lazarinrestful.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sample.app.lazarinrestful.service.TransactionManagerService;
import com.sample.app.lazarinrestful.vo.StatisticVO;
import com.sample.app.lazarinrestful.vo.TransactionVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Lazarin, Carlos
 *
 */
@Slf4j
@Service
public class TransactionManagerServiceImpl implements TransactionManagerService {

	private List<TransactionVO> transactionList;

	/**
	 * 
	 */
	public TransactionManagerServiceImpl() {
		if (CollectionUtils.isEmpty(transactionList)) {
			transactionList = new ArrayList<>();
		}
	}

	/**
	 * Creates a new transaction
	 * 
	 * @param transactionVO
	 *            {@link TransactionVO}
	 */
	public void createNewTransaction(TransactionVO transactionVO) {
		synchronized (this.transactionList) {
			log.info(String.format("New transaction successfully created [ transactionInfo=%s ]", transactionVO));
			transactionList.add(transactionVO);
		}
	}

	public StatisticVO provideTransactionsStatisticData() {

		log.debug("Starting providing statistic data.");

		synchronized (this.transactionList) {

			StatisticVO statisticVO = new StatisticVO();

			if (!this.transactionList.isEmpty()) {

				log.debug(String.format("Transaction list is not empty [size=%d]", this.transactionList.size()));
				
				
				DoubleSummaryStatistics statistics = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.map(transactionVO -> transactionVO.getAmount())
						.mapToDouble(BigDecimal::doubleValue)
						.collect(DoubleSummaryStatistics::new,
								DoubleSummaryStatistics::accept,
								DoubleSummaryStatistics::combine);
				
				
			//			.map(transactionVO -> transactionVO.getAmount());
					/*	.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, 
								DoubleSummaryStatistics::combine);*/
				
					/*	.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.map(transactionVO -> transactionVO.getAmount())*/
						
						
				/*		
						collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, 
								DoubleSummaryStatistics::combine);*/
				

			/*	BigDecimal sum = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.map(transactionVO -> transactionVO.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

				statisticVO.setSum(sum.setScale(2, BigDecimal.ROUND_HALF_UP));

				Optional<TransactionVO> max = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.max(Comparator.comparing(TransactionVO::getAmount));

				Optional<TransactionVO> min = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.min(Comparator.comparing(TransactionVO::getAmount));

				OptionalDouble average = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.map(transactionVO -> transactionVO.getAmount()).mapToDouble(BigDecimal::doubleValue).average();

				long count = transactionList.stream()
						.filter(transactionVO -> this.verifyIfTransactionIsNotTooOld(transactionVO))
						.collect(Collectors.counting());
*/
				//statisticVO.setAvg(average.isPresent() ? new BigDecimal(averageValue).setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				statisticVO.setAvg(new BigDecimal(statistics.getAverage()).setScale(2, BigDecimal.ROUND_HALF_UP));
			//	statisticVO.setMax(max.isPresent() ? max.get().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				statisticVO.setMax(new BigDecimal(statistics.getMax()).setScale(2, BigDecimal.ROUND_HALF_UP));
				//statisticVO.setMin(min.isPresent() ? min.get().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
				statisticVO.setMin(new BigDecimal(statistics.getMin()).setScale(2, BigDecimal.ROUND_HALF_UP));
				//statisticVO.setCount(count);
				statisticVO.setCount(statistics.getCount());
				//statisticVO.setSum(sum.setScale(2, BigDecimal.ROUND_HALF_UP));
				statisticVO.setSum(new BigDecimal(statistics.getSum()).setScale(2, BigDecimal.ROUND_HALF_UP));

			}

			log.debug(String.format("Ended providing statistic data - [statisticVO=%s]", statisticVO));

			return statisticVO;

		}
	}

	/**
	 * Verifies if given transaction is older than 60 seconds
	 * 
	 * @param transactionVO
	 * @return true/false
	 */
	public synchronized boolean verifyIfTransactionIsNotTooOld(TransactionVO transactionVO) {

		ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));

		long seconds = transactionVO.getTimestamp().until(zonedNow, ChronoUnit.SECONDS);
		
		log.debug(String.format("Verifying if transaction is too old - [differenceInSeconds=%d]",seconds));
		
		boolean isNotOld = seconds < 60;
		
		log.debug(String.format("Verifying if transaction is too old - [isNotOld=%b]",isNotOld));

		return isNotOld;
	}

	/**
	 * Deletes all transactions
	 */
	public void deleteAllTransactions() {
		log.info("Deleting all transactions");
		synchronized (this.transactionList) {
			this.transactionList.clear();
		}
	}
	
}