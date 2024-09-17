package com.money.reaper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.money.reaper.model.User;
import com.money.reaper.repository.UserRepository;
import com.money.reaper.util.UserStatus;
import com.money.reaper.util.UserType;

@EnableScheduling
@SpringBootApplication
public class ReaperApplication {

	@Autowired
	public PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ReaperApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				// Create a default admin user
				User user = new User();
				user.setBusinessName("Tech Business");
				user.setEmail("techadmin@gmail.com");
				user.setMobile("9999999999");
				user.setPassword(passwordEncoder.encode("Tech@admin#901"));
				user.setContactPersonName("admin");
				user.setUserType(UserType.ADMIN);
				user.setUserStatus(UserStatus.ACTIVE);
				user.setGstin("NA");
				user.setPan("NA");
				user.setWebsite("NA");
				user.setUniqueId("NA");
				user.setApiKey("NA");
				user.setAdminIpAddress("NA");
				user.setWebhookUrl("NA");
				user.setWhitelistedIps("NA");
				userRepository.save(user);
				System.out.println("Admin user created successfully.");
				System.out.println("Admin email: techadmin@gmail.com, pass: Tech@admin#901");
			} else {
				System.out.println("Admin user already exists.");
			}
		};
	}
}
