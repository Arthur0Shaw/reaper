package com.money.reaper.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StatusEnquiryRequest {

	@NotNull(message = "UniqueId is mandatory")
	@Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "UniqueId must be the one provided at the time of onboarding it is 16 characters long")
	private String uniqueId;

	@NotNull(message = "Hash is mandatory")
	@Pattern(regexp = "^[a-zA-Z0-9]{32}$", message = "Hash must be alphanumeric and 32 characters long")
	private String hash;

	private String merchantOrderId;
	private String pgOrderId;

}
