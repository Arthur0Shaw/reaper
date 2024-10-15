package com.money.reaper.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "acquirerMapping")
public class AcquirerMapping {

	@Id
	private String id;
	@NotBlank(message = "Bank is required")
	@Indexed
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
