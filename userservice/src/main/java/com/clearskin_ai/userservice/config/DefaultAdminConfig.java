package com.clearskin_ai.userservice.config;

import com.clearskin_ai.userservice.entity.User;
import com.clearskin_ai.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.admin.email}")
    private String adminEmail;

    @Value("${default.admin.name}")
    private String adminName;

    @Value("${default.admin.password}")
    private String adminPassword;

    @Value("${default.admin.role}")
    private String adminRole;

    @Bean
    public CommandLineRunner createDefaultAdmin() {
        return args -> {
            Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

            if (existingAdmin.isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setName(adminName);
                admin.setPassword(passwordEncoder.encode(adminPassword)); // ✅ encoded password
                admin.setRole(adminRole);
                admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                userRepository.save(admin);
                System.out.println("Default admin created: " + adminEmail);
            } else {
                System.out.println("Default admin already exists: " + adminEmail);
            }
        };
    }
}
