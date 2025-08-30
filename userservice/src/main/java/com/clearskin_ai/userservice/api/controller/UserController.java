package com.clearskin_ai.userservice.api.controller;

import com.clearskin_ai.userservice.api.dto.*;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.service.UserService;
import com.clearskin_ai.userservice.util.EndpointBundle;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto dto, Authentication authentication) {
        userService.changePassword(authentication.getName(), dto);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }




}