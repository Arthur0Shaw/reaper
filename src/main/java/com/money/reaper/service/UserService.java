package com.money.reaper.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import com.money.reaper.config.JwtTokenService;
import com.money.reaper.dto.LoginRequest;
import com.money.reaper.dto.LoginResponse;
import com.money.reaper.dto.UserRegistrationRequest;
import com.money.reaper.model.User;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.KeyGeneratorUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtService;


	public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = new JwtTokenService();
	}

	
	public User registerUser(UserRegistrationRequest request, HttpServletRequest httpRequest) {
		// Check if the email already exists
		Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}

		// Generate unique ID and API key
		String uniqueId = KeyGeneratorUtil.generateUniqueId();
		String apiKey = KeyGeneratorUtil.generateApiKey();

		// Create new user
		User user = new User();
		user.setBusiness_name(request.getBusiness_name());
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());
		user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt the password
		user.setContact_person_name(request.getContact_person_name());
		user.setGstin(request.getGstin());
		user.setPan(request.getPan());
		user.setWebsite(request.getWebsite());

		// Set additional fields
		user.setUniqueId(uniqueId);
		user.setApiKey(apiKey);
		user.setUserType("MERCHANT");
		user.setUserStatus("ACTIVE");
		user.setAdminIpAddress(getClientIpAddress(httpRequest)); // Store admin's IP address

		return userRepository.save(user);
	}

//	public LoginResponse authenticateUser(LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		String token = jwtService.generateToken(userDetails);
//
//        return new LoginResponse(token, "Login successful");
//    }

//    private String generateToken(UserDetails userDetails) {
//        // Implement your token generation logic here
//        return "dummy-token"; // Replace with actual token generation
//    }
    

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

		// Check if the password matches
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid email or password");
		}
		String jwtToken = jwtService.generateToken(user);

		return new LoginResponse(jwtToken, "Login successful");
	}
	
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	// Utility method to get the client's IP address
	private String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
