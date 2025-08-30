package com.clearskinai.adminservice.api.controller;

import com.clearskinai.adminservice.api.dto.AppointmentAdminRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentResponseDto;
import com.clearskinai.adminservice.client.clientDtos.UserDto;
import com.clearskinai.adminservice.config.JwtUtil;
import com.clearskinai.adminservice.enums.AppointmentStatus;
import com.clearskinai.adminservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AppointmentResponseDto> createAppointmentByUser(
            @Valid @RequestBody AppointmentRequestDto dto,
            Authentication authentication) {
        String email = authentication.getName(); // Extract email from JWT
        return new ResponseEntity<>(appointmentService.createAppointmentByUser(dto, email), HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AppointmentResponseDto> createAppointmentByAdmin(
            @Valid @RequestBody AppointmentAdminRequestDto dto) {
        return new ResponseEntity<>(appointmentService.createAppointmentByAdmin(dto), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByUserId(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(pageable));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, status));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Page<AppointmentResponseDto>> searchAppointments(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDateTime date,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.searchAppointments(userName, userEmail, status, date, pageable));
    }

    @GetMapping("/user/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AppointmentResponseDto>> searchUserAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDateTime date,
            Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        UserDto user = appointmentService.getUserByEmail(email); // Assume service can call UserServiceClient
        return ResponseEntity.ok(appointmentService.searchUserAppointments(user.getUserId(), status, date, pageable));
    }

    @PutMapping("/user/{userId}/{appointmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentByUser(
            @PathVariable Long userId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentRequestDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                appointmentService.updateAppointmentByUser(appointmentId, userId, dto, email)
        );
    }

    @DeleteMapping("/user/{userId}/{appointmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteAppointmentByUser(
            @PathVariable Long userId,
            @PathVariable Long appointmentId,
            Authentication authentication) {
        String email = authentication.getName();
        appointmentService.deleteAppointmentByUser(appointmentId, userId, email);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

    @PutMapping("/admin-staff/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentByAdminStaff(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentRequestDto dto) {
        return ResponseEntity.ok(
                appointmentService.updateAppointmentByAdminStaff(appointmentId, dto)
        );
    }

    @DeleteMapping("/admin-staff/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<String> deleteAppointmentByAdminStaff(
            @PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentByAdminStaff(appointmentId);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

}