package com.clearskinai.adminservice.api.dto;

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
public class AppointmentRequestDto {

    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 100, message = "User name must be between 3 and 100 characters")
    private String userName;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotBlank(message = "Doctor name is required")
    @Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
    private String doctorName;
}
