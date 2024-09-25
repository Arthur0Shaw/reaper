package com.money.reaper.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcquirerRoutingDetailsRequest {

	@NotBlank(message = "Bank is required")
	private String bank;
	@NotBlank(message = "TotalTxnCount is required")
	private String totalTxnCount;
	@NotBlank(message = "MaximumTxnAmount is required")
	private String maximumTxnAmount;
	@NotBlank(message = "ThresholdTxnAmount is required")
	private String thresholdTxnAmount;

}
