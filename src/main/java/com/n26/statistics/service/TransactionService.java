package com.n26.statistics.service;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transaction;

public interface TransactionService {

	public void addTransaction(Transaction transaction);
	public Statistics calcStatistics();
	public void clearData();

}
