package com.money.reaper.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction")
public class Transaction {

	@Id
	private String id;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits")
	@NotBlank(message = "Mobile number is required")
	private String mobile;

	@NotBlank(message = "Merchant UPI ID is required")
	private String merchant_upi_id;

	private String acquirer_reference_id;
	private String rrn;
	private String acquirer_response_code;
	private String acquirer_response_message;

	@NotBlank(message = "Amount is required")
	@Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Amount should be a valid number with two decimal places")
	private String amount;

	@NotBlank(message = "Merchant order ID is required")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Merchant order ID must be alphanumeric")
	private String merchant_order_id;

	@NotBlank(message = "Name is required")
	@Size(min = 2, message = "Name should have at least 2 characters")
	private String name;

	@NotBlank(message = "Status is required")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Status must contain only alphabetic characters")
	private String status;

	@Size(max = 255, message = "UDF1 should be at most 255 characters")
	private String udf1;

	@Size(max = 255, message = "UDF2 should be at most 255 characters")
	private String udf2;

	@Size(max = 255, message = "UDF3 should be at most 255 characters")
	private String udf3;

	@Size(max = 255, message = "UDF4 should be at most 255 characters")
	private String udf4;

	@NotBlank(message = "Created_at is required")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Created at date must be in the format YYYY-MM-DD HH:mm:ss")
	private LocalDateTime created_at;

	@NotBlank(message = "Updated_at is required")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Updated at date must be in the format YYYY-MM-DD HH:mm:ss")
	private LocalDateTime updated_at;

	@NotBlank(message = "Updated_at is required")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Updated at date must be in the format YYYY-MM-DD")
	private LocalDate date_index;

}