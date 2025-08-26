package com.clearskin_ai.userservice.service.Impl;

import com.clearskin_ai.userservice.api.dto.*;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.entity.User;
import com.clearskin_ai.userservice.enums.Roles;
import com.clearskin_ai.userservice.exception.InvalidCredentialsException;
import com.clearskin_ai.userservice.exception.InvalidRoleException;
import com.clearskin_ai.userservice.exception.PasswordMismatchException;
import com.clearskin_ai.userservice.exception.UserAlreadyExistsException;
import com.clearskin_ai.userservice.exception.UserNotFoundException;
import com.clearskin_ai.userservice.repository.UserRepository;
import com.clearskin_ai.userservice.service.EmailService;
import com.clearskin_ai.userservice.service.UserService;
import com.clearskin_ai.userservice.config.JwtUtil;
import com.clearskin_ai.userservice.util.EmailTemplateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(ApplicationConstants.USER_ALREADY_EXISTS);
        }

        if (!dto.getPassword().equals(dto.getRetypePassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encryptedPassword);
        user.setRole(dto.getRole());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        String bodyContent = "<p>Hi <strong>" + dto.getName() + "</strong>,</p>"
                + "<p>Welcome to ClearSkin AI! Your account has been created.</p>"
                + "<p>Email: " + dto.getEmail() + "</p>";
        String body = emailTemplateUtil.buildEmail("Welcome to ClearSkin AI!", bodyContent);
        emailService.sendEmail(dto.getEmail(), ApplicationConstants.EMAIL_SUBJECT_WELCOME, body);
    }

    @Transactional
    @Override
    public void adminRegisterUser(AdminRegisterUserDto dto) {
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(ApplicationConstants.USER_ALREADY_EXISTS);
        }

        try {
            Roles.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: must be ADMIN or STAFF");
        }

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(encryptedPassword);
        user.setRole(dto.getRole().toUpperCase());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        String credentialsHtml = emailTemplateUtil.buildCredentialsSection(dto.getEmail(), rawPassword);
        String body = emailTemplateUtil.buildEmail("Welcome to ClearSkin AI!", credentialsHtml);
        emailService.sendEmail(dto.getEmail(), ApplicationConstants.EMAIL_SUBJECT_WELCOME, body);
    }

    @Override
    public LoginResponseDto loginUser(LoginUserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ApplicationConstants.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(ApplicationConstants.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new LoginResponseDto(user.getEmail(), token);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public List<UserDto> searchUsers(String query) {
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return convertToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    @Override
    public void changePassword(String email, ChangePasswordDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // check old password
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        // check new passwords match
        if (!dto.getNewPassword().equals(dto.getRetypeNewPassword())) {
            throw new PasswordMismatchException("New passwords do not match");
        }

        // encrypt and save
        String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

}