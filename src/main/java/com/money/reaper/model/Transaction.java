package com.money.reaper.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.money.reaper.util.TransactionStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "UniqueId is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "UniqueId must be the one provided at the time of onboarding it is 16 characters long")
    private String uniqueId;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits")
    @NotBlank(message = "Mobile number is required")
    private String mobile;

    @NotBlank(message = "Merchant UPI ID is required")
    private String merchantUpiId;

    private String acquirerReferenceId;
    private String rrn;
    
    private String pgResponseCode;
    private String pgResponseMessage;
    private String acquirerResponseCode;
    private String acquirerResponseMessage;

    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Amount should be a valid number with two decimal places")
    private String amount;

    @NotBlank(message = "Merchant order ID is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Merchant order ID must be alphanumeric")
    private String merchantOrderId;

    @NotBlank(message = "PG order ID is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "PG order ID must be alphanumeric")
    private String pgOrderId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Status must contain only alphabetic characters")
    private TransactionStatus status;

    @Size(max = 255, message = "UDF1 should be at most 255 characters")
    private String udf1;

    @Size(max = 255, message = "UDF2 should be at most 255 characters")
    private String udf2;

    @Size(max = 255, message = "UDF3 should be at most 255 characters")
    private String udf3;

    @Size(max = 255, message = "UDF4 should be at most 255 characters")
    private String udf4;
    
    private String intentURL;

    @NotBlank(message = "Created_at is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Created at date must be in the format YYYY-MM-DD HH:mm:ss")
    private String createdAt;

    @NotBlank(message = "Updated_at is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Updated at date must be in the format YYYY-MM-DD HH:mm:ss")
    private String updatedAt;

    @NotBlank(message = "Updated_at is required")
    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Updated at date must be in the format YYYYMMDD")
    private String dateIndex;
}
