package com.clearskin_ai.userservice.service;

import com.clearskin_ai.userservice.api.dto.AdminRegisterUserDto;
import com.clearskin_ai.userservice.api.dto.LoginUserDto;
import com.clearskin_ai.userservice.api.dto.RegisterUserDto;

public interface UserService {
    void registerUser(RegisterUserDto user);
    void adminRegisterUser(AdminRegisterUserDto user);
    LoginUserDto loginUser(LoginUserDto user);
}
