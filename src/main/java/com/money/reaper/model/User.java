package com.money.reaper.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Business name is required")
    private String business_name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits")
    @NotBlank(message = "Mobile number is required")
    private String mobile;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Contact person name is required")
    private String contact_person_name;

    // Non-mandatory fields
    private String gstin;
    private String pan;
    private String website;

    // New fields
    private String userType;
    private String userStatus;
    private String uniqueId;
    private String apiKey;
    private String adminIpAddress;  // IP address of the admin who created the user
}
