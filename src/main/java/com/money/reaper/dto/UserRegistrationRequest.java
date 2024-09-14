package com.money.reaper.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationRequest {

	@NotBlank(message = "Business name is required")
	private String business_name;

	@Email(message = "Invalid email address")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
	private String mobile;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Contact person name is required")
	private String contact_person_name;

	private String gstin;
	private String pan;
	private String website;
}
