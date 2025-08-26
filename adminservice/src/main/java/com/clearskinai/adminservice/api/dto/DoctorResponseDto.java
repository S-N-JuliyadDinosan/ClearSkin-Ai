package com.clearskinai.adminservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDto {
    private Long doctorId;
    private String name;
    private String qualifications;
    private String speciality;
}