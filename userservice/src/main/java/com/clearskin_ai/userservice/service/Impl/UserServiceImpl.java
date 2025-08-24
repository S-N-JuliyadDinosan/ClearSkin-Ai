package com.clearskin_ai.userservice.service.Impl;

import com.clearskin_ai.userservice.api.dto.AdminRegisterUserDto;
import com.clearskin_ai.userservice.api.dto.LoginResponseDto;
import com.clearskin_ai.userservice.api.dto.LoginUserDto;
import com.clearskin_ai.userservice.api.dto.RegisterUserDto;
import com.clearskin_ai.userservice.config.JwtUtil;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.entity.User;
import com.clearskin_ai.userservice.enums.Roles;
import com.clearskin_ai.userservice.exception.*;
import com.clearskin_ai.userservice.repository.UserRepository;
import com.clearskin_ai.userservice.service.EmailService;
import com.clearskin_ai.userservice.service.UserService;
import com.clearskin_ai.userservice.util.EmailTemplateUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final EmailTemplateUtil emailTemplateUtil;

    @Transactional
    @Override
    public void registerUser(RegisterUserDto dto) {
        // Validate email
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(ApplicationConstants.USER_ALREADY_EXISTS);
        }

        // Validate passwords match
        if (!dto.getPassword().equals(dto.getRetypePassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // Save user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encryptedPassword);
        user.setRole(dto.getRole());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        // Send email without password
        String bodyContent = "<p>Hi <strong>" + dto.getName() + "</strong>,</p>"
                + "<p>Welcome to ClearSkin AI! Your account has been created.</p>"
                + "<p>Email: " + dto.getEmail() + "</p>";
        String body = emailTemplateUtil.buildEmail("Welcome to ClearSkin AI!", bodyContent);
        emailService.sendEmail(dto.getEmail(), ApplicationConstants.EMAIL_SUBJECT_WELCOME, body);
    }

    @Transactional
    @Override
    public void adminRegisterUser(AdminRegisterUserDto dto) {
        // Validate email
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(ApplicationConstants.USER_ALREADY_EXISTS);
        }

        // Validate role against enum
        try {
            Roles.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: must be ADMIN or STAFF");
        }

        // Generate random password
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // Save user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encryptedPassword);
        user.setRole(dto.getRole().toUpperCase());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        // Send email with credentials using reusable template
        String credentialsHtml = emailTemplateUtil.buildCredentialsSection(dto.getEmail(), rawPassword);
        String body = emailTemplateUtil.buildEmail("Welcome to ClearSkin AI!", credentialsHtml);
        emailService.sendEmail(dto.getEmail(), ApplicationConstants.EMAIL_SUBJECT_WELCOME, body);
    }

    @Override
    public LoginResponseDto loginUser(LoginUserDto dto) {
        // Find user by email
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ApplicationConstants.USER_NOT_FOUND));

        // Verify password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(ApplicationConstants.INVALID_CREDENTIALS);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Return response without password
        return new LoginResponseDto(user.getEmail(), token);
    }
}
