package com.clearskinai.adminservice.api.dto;

import com.clearskinai.adminservice.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
    private Long appointmentId;
    private Long userId;
    private String userEmail;
    private String userName;
    private LocalDateTime date;
    private Long doctorId;
    private String doctorName;
    private AppointmentStatus status;
}