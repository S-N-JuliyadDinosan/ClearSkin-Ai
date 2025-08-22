package com.clearskin_ai.userservice.service.Impl;

import com.clearskin_ai.userservice.api.dto.AdminRegisterUserDto;
import com.clearskin_ai.userservice.api.dto.LoginUserDto;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.entity.User;
import com.clearskin_ai.userservice.exception.InvalidCredentialsException;
import com.clearskin_ai.userservice.exception.PasswordMismatchException;
import com.clearskin_ai.userservice.exception.UserAlreadyExistsException;
import com.clearskin_ai.userservice.exception.UserNotFoundException;
import com.clearskin_ai.userservice.repository.UserRepository;
import com.clearskin_ai.userservice.api.dto.RegisterUserDto;
import com.clearskin_ai.userservice.service.EmailService;
import com.clearskin_ai.userservice.service.UserService;
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
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        // Send welcome email (without password)
        String body = String.format(ApplicationConstants.EMAIL_BODY_TEMPLATE_USER, dto.getName(), dto.getEmail());
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

        // Generate random password
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // Save user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encryptedPassword);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        // Send email with credentials
        String body = String.format(ApplicationConstants.EMAIL_BODY_TEMPLATE_ADMIN, dto.getName(), dto.getEmail(), rawPassword);
        emailService.sendEmail(dto.getEmail(), ApplicationConstants.EMAIL_SUBJECT_WELCOME, body);
    }

    @Override
    public LoginUserDto loginUser(LoginUserDto dto) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(ApplicationConstants.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(ApplicationConstants.INVALID_CREDENTIALS);
        }

        // Return LoginUserDto with user details
        return new LoginUserDto(user.getEmail(), null); // Password is set to null for security
    }
}