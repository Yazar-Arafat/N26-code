package com.n26.statistics.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.service.TransactionService;

@RestController
public class StatisticsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

	@Autowired
	TransactionService transactionService;

	@RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {

		Instant instant = Instant.now();
		long timeNow = instant.toEpochMilli();

		if (transaction.getTimestamp() > (timeNow - 60000)) {
			transactionService.addTransaction(transaction);
			return ResponseEntity.status(201).build();
		} else {
			return ResponseEntity.status(204).build();
		}

	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public Statistics getStats() {
		LOGGER.info("Fetching the transactions which happened over the last 60 seconds");
		return transactionService.calcStatistics();
	}

}
