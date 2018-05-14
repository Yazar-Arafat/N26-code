package com.n26.statistics.serviceimpl.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.service.TransactionService;

import net.minidev.json.JSONValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceImplTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	TransactionService transactionService;

	private void addTestTransaction(Transaction transaction) throws Exception {
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(JSONValue.toJSONString(transaction))).andExpect(status().is(201));
	}

	@Before
	public void setUp() throws Exception {
		Instant instant = Instant.now();
		long timeNow = instant.toEpochMilli();

		addTestTransaction(new Transaction(50, timeNow));
		for (int i = 0; i < 8; i++) {
			addTestTransaction(new Transaction(100, timeNow + 1 + i));
		}

		addTestTransaction(new Transaction(150, timeNow + 20));
	}

	@After
	public void tearDown() throws Exception {
		transactionService.clearData();
	}

	@Test
	public void calcStatistics() throws Exception {
		Statistics stats = transactionService.calcStatistics();

		assertEquals(150, stats.getMax(), 0);
		assertEquals(10, stats.getMin(), 0);
		assertEquals(92.5, stats.getAvg(), 0);
		assertEquals(12, stats.getCount(), 0);
		assertEquals(1110, stats.getSum(), 0);

	}

}