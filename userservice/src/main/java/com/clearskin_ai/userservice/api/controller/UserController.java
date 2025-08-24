package com.clearskin_ai.userservice.api.controller;

import com.clearskin_ai.userservice.api.dto.LoginResponseDto;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.api.dto.AdminRegisterUserDto;
import com.clearskin_ai.userservice.api.dto.LoginUserDto;
import com.clearskin_ai.userservice.api.dto.RegisterUserDto;
import com.clearskin_ai.userservice.service.UserService;
import com.clearskin_ai.userservice.util.EndpointBundle;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.USER_BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserDto user) {
        userService.registerUser(user);
        return new ResponseEntity<>(ApplicationConstants.COMMON_USER_CREATED_SUCCESSFULLY, HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminRegisterUser(@Valid @RequestBody AdminRegisterUserDto user) {
        userService.adminRegisterUser(user);
        return new ResponseEntity<>(ApplicationConstants.ADMIN_USER_CREATED_SUCCESSFULLY, HttpStatus.CREATED);
    }

    @PostMapping(EndpointBundle.LOGIN_USER)
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginUserDto user) {
        LoginResponseDto loggedUser = userService.loginUser(user);
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }
}