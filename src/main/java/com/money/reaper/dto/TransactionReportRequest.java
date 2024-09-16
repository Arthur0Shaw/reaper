package com.money.reaper.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionReportRequest {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDate dateIndexStart;
	private LocalDate dateIndexEnd;
	private String status;
	private String id;
	private String acquirerReferenceId;
	private String merchantOrderId;

}
