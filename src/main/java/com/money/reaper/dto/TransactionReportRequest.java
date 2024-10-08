package com.money.reaper.dto;

import com.money.reaper.util.TransactionStatus;

import lombok.Data;

@Data
public class TransactionReportRequest {

	private String dateIndexFrom;
	private String dateIndexTo;
	private TransactionStatus status;
	private String id;
	private String acquirerReferenceId;
	private String merchantOrderId;

}
