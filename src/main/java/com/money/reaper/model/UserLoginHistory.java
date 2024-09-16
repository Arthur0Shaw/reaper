package com.money.reaper.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.money.reaper.util.UserType;

import lombok.Data;

@Data
public class UserLoginHistory {

	@Id
	private String id;

	@Column(name = "username")
	private String userName;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_role")
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column(name = "description")
	private String description;

	private String ipAddress;
	private String os;
	private String browser;
	private String createdAt;

}
