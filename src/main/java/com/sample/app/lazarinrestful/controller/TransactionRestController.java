package com.sample.app.lazarinrestful.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.app.lazarinrestful.service.TransactionManagerService;
import com.sample.app.lazarinrestful.vo.StatisticVO;
import com.sample.app.lazarinrestful.vo.TransactionVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Lazarin, Carlos
 *
 */
@Slf4j
@RestController
public class TransactionRestController {
	
	@Autowired
	private TransactionManagerService transactionManagerService;
	
	@ApiOperation(value = "Creates a new transaction.", response = Void.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Success"), 
			@ApiResponse(code = 204, message = "Transaction is older than 60 seconds"), 
			@ApiResponse(code = 400, message = "Invalid payload"),
			@ApiResponse(code = 422, message = "Any of the fields are not parsable/the transaction date is in the future")})
	@PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createNewTransaction(@Valid @RequestBody TransactionVO transactionVO){
		
		if(!validateRequest(transactionVO)) {
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		
		if(!transactionManagerService.verifyIfTransactionIsNotTooOld(transactionVO)) {
			log.info(String.format("Tried to create a new transaction but it is too old [ transactionInfo=%s ]",transactionVO));
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		log.info(String.format("Creating new transaction [ transactionInfo=%s ]",transactionVO));
		this.transactionManagerService.createNewTransaction(transactionVO);
		return new ResponseEntity<Void>(HttpStatus.CREATED);		
	}

	@ApiOperation(value = "Returns the statistics based on the transactions that happened in the last 60 seconds.", response = StatisticVO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Success", response = StatisticVO.class)
			})
	@GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatisticVO> getStatistics(){
		return new ResponseEntity<StatisticVO> (this.transactionManagerService.provideTransactionsStatisticData(), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Deletes all existent transactions")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "All transactions deleted successfully")
			})
	@DeleteMapping("/transactions")
	public ResponseEntity<Void> deleteTransactions(){
		this.transactionManagerService.deleteAllTransactions();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);	
	}
	
	/**
	 * Validates request's time stamp value
	 * 
	 * @param transactionVO
	 * @return true/false
	 */
	private boolean validateRequest(TransactionVO transactionVO) {
		boolean isValidTimeStamp = true;
		
		DateTimeFormatter parser = new DateTimeFormatterBuilder()
			    .appendOptional(DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("Etc/UTC")))
			    .toFormatter();
		
		try {
			parser.parse(transactionVO.getTimestamp().toString());
		} catch (Exception e) {
			log.error(String.format("Provided timestamp is not in a valid format [transactionVO.getTimestamp=%s",transactionVO));
			isValidTimeStamp = false;
		}
		
		ZonedDateTime zonedNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Etc/UTC"));
				
		boolean isAfter = transactionVO.getTimestamp().isAfter(zonedNow);
		if(isAfter) {
			isValidTimeStamp = false;
		}
		
		return isValidTimeStamp;
	}
}
