package com.money.reaper.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "acquirerRoutingDetails")
public class AcquirerRoutingDetails {

	@Id
	private String id;
	@NotBlank(message = "Bank is required")
	private String bank;
	@NotBlank(message = "TotalTxnCount is required")
	private String totalTxnCount;
	@NotBlank(message = "MaximumTxnAmount is required")
	private String maximumTxnAmount;
	@NotBlank(message = "ThresholdTxnAmount is required")
	private String thresholdTxnAmount;

}
