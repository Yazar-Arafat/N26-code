package com.n26.statistics.service.impl;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	private long count;
	private double max;
	private double avg;
	private double min;
	private double sum;
	Instant instant;
	long timeNow;

	ConcurrentHashMap<Long, Double> data;

	public TransactionServiceImpl() {
		data = new ConcurrentHashMap<>();
		this.count = 0;
		this.max = 0;
		this.avg = 0;
		this.min = 10000000;
		this.sum = 0;
		this.instant = Instant.now();
		timeNow = this.instant.toEpochMilli();

	}
	
	public ConcurrentHashMap<Long, Double> getData() {return data;}

	public void setData(ConcurrentHashMap<Long, Double> data) {this.data = data;}

	/**
	 *  Add statistics from Transaction
	 *  
	 */
	@Override
	public synchronized void addTransaction(Transaction transaction) {
		this.data.put(transaction.getTimestamp(), transaction.getAmount());
	}
	
	/**
	 * Get statistics
	 * 
	 * Time complexity: O(1)
	 */
	@Override
	public Statistics calcStatistics() {
		calculateStats();
		return new Statistics(this.sum, this.avg, this.max, this.min, this.count);
	}
	
	 private synchronized void calculateStats(){
	    long currentTime = Instant.now().toEpochMilli();

	    LOGGER.info("Filter out old values older than 60 seconds");
	    List<Double> filteredList = data
	              .entrySet()
	              .stream()
	              .filter(p -> p.getKey().longValue()>currentTime-60000)
	              .map(p->p.getValue()).collect(Collectors.toList());


	    DoubleSummaryStatistics stats = filteredList.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

	     this.avg = stats.getAverage();
	     this.count = stats.getCount();
	     this.max = stats.getMax();
	     this.sum = stats.getSum();
	     this.min = stats.getMin();

	    }
	 
	 	/**
		 * Clear Data
		 * 
		 */
		@Override
		public void clearData() {
			data = new ConcurrentHashMap<>();
		}
}
