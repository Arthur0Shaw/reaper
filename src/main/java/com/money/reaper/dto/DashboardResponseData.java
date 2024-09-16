package com.money.reaper.dto;

import java.util.Map;

import lombok.Data;

@Data
public class DashboardResponseData {

	private Long totalCount;

	private Double totalAmount;

	private Map<String, Long> dateWiseTransactionCount;

	private Map<String, Double> dateWiseTransactionAmount;

}
