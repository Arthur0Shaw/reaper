package com.money.reaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.money.reaper.model.User;
import com.money.reaper.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class ReaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReaperApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				// Create a default admin user
				User user = new User();
				user.setBusiness_name("Tech Business");
				user.setEmail("techadmin@gmail.com");
				user.setMobile("9999999999");
				user.setPassword("Tech@admin#901"); 
				user.setContact_person_name("admin");
				user.setUserType("ADMIN");
				user.setUserStatus("ACTIVE");
				user.setGstin("NA");
				user.setPan("NA");
				user.setWebsite("NA");
				user.setUniqueId("NA");
				user.setApiKey("NA");
				user.setAdminIpAddress("NA");
				user.setWebhoook_url("NA");
				user.setWhitelisted_ips("NA");
				userRepository.save(user);
				System.out.println("Admin user created successfully.");
                System.out.println("Admin email: techadmin@gmail.com, pass: Tech@admin#901");
			} else {
				System.out.println("Admin user already exists.");
			}
		};
	}
}
