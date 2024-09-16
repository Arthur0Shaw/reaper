package com.money.reaper.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.reaper.config.JwtTokenService;
import com.money.reaper.dto.LoginRequest;
import com.money.reaper.dto.LoginResponse;
import com.money.reaper.dto.UserRegistrationRequest;
import com.money.reaper.model.User;
import com.money.reaper.model.UserLoginHistory;
import com.money.reaper.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

	private final UserService userService;

	@Autowired
	public JwtTokenService jwtTokenService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationRequest request,
			HttpServletRequest httpRequest) {
		User user = userService.registerUser(request, httpRequest);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
			HttpServletRequest httpRequest) {
		LoginResponse loginResponse = userService.login(request, httpRequest);
		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.substring(7);
		jwtTokenService.invalidateToken(token);
		return ResponseEntity.ok("Logged out successfully");
	}

	@GetMapping("/merchantList")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllMerchants();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/loginHistory")
	public ResponseEntity<List<UserLoginHistory>> getAllLoginHistories() {
		List<UserLoginHistory> loginHistory = userService.getAllLoginHistories();
		return ResponseEntity.ok(loginHistory);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
