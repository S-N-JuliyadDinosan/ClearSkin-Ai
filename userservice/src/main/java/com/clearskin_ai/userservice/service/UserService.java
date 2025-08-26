package com.clearskin_ai.userservice.service;

import com.clearskin_ai.userservice.api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void registerUser(RegisterUserDto user);
    void adminRegisterUser(AdminRegisterUserDto user);
    LoginResponseDto loginUser(LoginUserDto dto);
    Page<UserDto> getAllUsers(Pageable pageable);
    List<UserDto> searchUsers(String query);
    UserDto getUserById(Long id);
    void deleteUser(Long id);
    void changePassword(String email, ChangePasswordDto dto);

}