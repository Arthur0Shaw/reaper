package com.money.reaper.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcquirerMappingRequest {

	@NotBlank(message = "Bank is required")
	private String bank;
	@NotBlank(message = "Username is required")
	private String username;
	@NotBlank(message = "Password is required")
	private String password;
	@NotBlank(message = "Vpa is required")
	private String vpa;
	private String adf1;
	private String adf2;
}
