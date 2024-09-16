package com.money.reaper.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InitiateTransactionRequest {

	@NotNull(message = "UniqueId is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "UniqueId must be the one provided at the time of onboarding it is 16 characters long")
	private String uniqueId;
	
    @NotNull(message = "Hash is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{32}$", message = "Hash must be alphanumeric and 32 characters long")
    private String hash;

    @NotNull(message = "Client Transaction ID is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,64}$", message = "Client Transaction ID must be alphanumeric and between 6 to 64 characters long")
    private String client_txn_id;

    @NotNull(message = "Amount is mandatory")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Amount must be a valid number with up to 2 decimal places")
    private String amount;

    @NotNull(message = "Product Info is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Product Info must be alphanumeric")
    private String product_info;

    @NotNull(message = "Customer Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Customer Name must contain only alphabets and spaces")
    private String customer_name;

    @NotNull(message = "Customer Email is mandatory")
    @Email(message = "Customer Email must be a valid email")
    private String customer_email;

    @NotNull(message = "Customer Mobile is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Customer Mobile must be a 10-digit number")
    private String customer_mobile;

    @NotNull(message = "Redirect URL is mandatory")
    @URL(message = "Redirect URL must be a valid URL")
    private String redirect_url;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "UDF1 must be alphanumeric")
    private String udf1;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "UDF2 must be alphanumeric")
    private String udf2;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "UDF3 must be alphanumeric")
    private String udf3;
}
