package com.clearskinai.adminservice.service;

import com.clearskinai.adminservice.api.dto.AppointmentAdminRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentResponseDto;
import com.clearskinai.adminservice.client.clientDtos.UserDto;
import com.clearskinai.adminservice.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDto createAppointmentByUser(AppointmentRequestDto dto, String authenticatedEmail);
    AppointmentResponseDto createAppointmentByAdmin(AppointmentAdminRequestDto dto);
    List<AppointmentResponseDto> getAppointmentsByUserId(Long userId);
    AppointmentResponseDto getAppointmentById(Long appointmentId);
    Page<AppointmentResponseDto> getAllAppointments(Pageable pageable);
    AppointmentResponseDto updateAppointmentStatus(Long appointmentId, AppointmentStatus status);
    Page<AppointmentResponseDto> searchAppointments(String userName, String userEmail, AppointmentStatus status, LocalDateTime date, Pageable pageable);
    Page<AppointmentResponseDto> searchUserAppointments(Long userId, AppointmentStatus status, LocalDateTime date, Pageable pageable);
    UserDto getUserByEmail(String email);
    AppointmentResponseDto updateAppointmentByUser(Long appointmentId, Long userId, AppointmentRequestDto dto, String authenticatedEmail);
    void deleteAppointmentByUser(Long appointmentId, Long userId, String authenticatedEmail);
    AppointmentResponseDto updateAppointmentByAdminStaff(Long appointmentId, AppointmentRequestDto dto);
    void deleteAppointmentByAdminStaff(Long appointmentId);
}
