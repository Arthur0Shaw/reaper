package com.money.reaper.dto;

import java.time.LocalDateTime;

import com.money.reaper.util.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiateTransactionResponse {

	private String id;
	private String merchantUpiId;
	private String acquirerReferenceId;
	private String rrn;
	private String pgResponseCode;
	private String pgResponseMessage;
	private String acquirerResponseCode;
	private String acquirerResponseMessage;
	private String amount;
	private String merchantOrderId;
	private String pgOrderId;
	private String intentURL;
	private TransactionStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
