package com.clearskinai.adminservice.api.dto;

import com.clearskinai.adminservice.enums.AppointmentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAdminRequestDto {
    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 100, message = "User name must be between 3 and 100 characters")
    private String userName;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
    private String doctorName;

    @NotNull(message = "Status is required")
    private AppointmentStatus status;
}