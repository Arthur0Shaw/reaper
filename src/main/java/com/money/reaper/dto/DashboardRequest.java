package com.money.reaper.dto;

import com.money.reaper.util.TransactionStatus;

import lombok.Data;

@Data
public class DashboardRequest {

	private String dateIndexFrom;
	private String dateIndexTo;
	private TransactionStatus status;

}
