package com.money.reaper.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.money.reaper.config.JwtTokenService;
import com.money.reaper.dto.LoginRequest;
import com.money.reaper.dto.LoginResponse;
import com.money.reaper.dto.UserRegistrationRequest;
import com.money.reaper.model.User;
import com.money.reaper.model.UserLoginHistory;
import com.money.reaper.repository.UserLoginHistoryRepository;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.DateTimeCreator;
import com.money.reaper.util.KeyGeneratorUtil;
import com.money.reaper.util.UserStatus;
import com.money.reaper.util.UserType;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenService jwtService;

	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;

	public UserService(UserRepository userRepository, UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
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
		user.setBusinessName(request.getBusiness_name());
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());
		user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt the password
		user.setContactPersonName(request.getContact_person_name());
		user.setGstin(request.getGstin());
		user.setPan(request.getPan());
		user.setWebsite(request.getWebsite());
		user.setWebhookUrl(request.getWebhook_url());
		user.setWhitelistedIps(request.getWhitelisted_ips());
		user.setBanks(request.getBanks());

		// Set additional fields
		user.setUniqueId(uniqueId);
		user.setApiKey(apiKey);
		user.setUserType(UserType.MERCHANT);
		user.setUserStatus(UserStatus.ACTIVE);
		user.setAdminIpAddress(getClientIpAddress(httpRequest)); // Store admin's IP address

		return userRepository.save(user);
	}

	public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

		// Check if the password matches
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			saveLoginHistory(user, "Invalid email or password", httpRequest);
			throw new IllegalArgumentException("Invalid email or password");
		}
		String jwtToken = jwtService.generateToken(user);

		saveLoginHistory(user, "Login successful", httpRequest);
		return new LoginResponse(jwtToken, "Login successful");
	}

	private void saveLoginHistory(User user, String message, HttpServletRequest httpRequest) {
		UserLoginHistory history = new UserLoginHistory();
		history.setUserName(user.getBusinessName());
		history.setUserId(user.getUniqueId());
		history.setUserType(user.getUserType());
		history.setDescription(message);
		history.setIpAddress(getClientIpAddress(httpRequest));
		history.setOs(getClientOs(httpRequest));
		history.setBrowser(getClientBrowser(httpRequest));
		history.setCreatedAt(DateTimeCreator.getDateTime());

		userLoginHistoryRepository.save(history);
	}

	// Fetch only merchants with status active
	public List<User> getAllMerchants() {
		return userRepository.findByUserTypeAndUserStatus(UserType.MERCHANT, UserStatus.ACTIVE);
	}
	
    public List<UserLoginHistory> getAllLoginHistories() {
        return userLoginHistoryRepository.findAll();
    }

	// Utility method to get the client's IP address
	private String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String getClientOs(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null) {
			if (userAgent.contains("Windows")) {
				return "Windows";
			} else if (userAgent.contains("Mac")) {
				return "Mac OS";
			} else if (userAgent.contains("X11")) {
				return "Unix";
			} else if (userAgent.contains("Android")) {
				return "Android";
			} else if (userAgent.contains("iPhone")) {
				return "iOS";
			}
		}
		return "Unknown OS";
	}

	private String getClientBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null) {
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				return "Internet Explorer";
			} else if (userAgent.contains("Edge")) {
				return "Edge";
			} else if (userAgent.contains("Firefox")) {
				return "Firefox";
			} else if (userAgent.contains("Chrome")) {
				return "Chrome";
			} else if (userAgent.contains("Safari")) {
				return "Safari";
			}
		}
		return "Unknown Browser";
	}
}
