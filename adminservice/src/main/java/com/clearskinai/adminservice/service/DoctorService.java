package com.clearskinai.adminservice.service;

import com.clearskinai.adminservice.api.dto.DoctorRequestDto;
import com.clearskinai.adminservice.api.dto.DoctorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto dto);
    DoctorResponseDto getDoctorById(Long doctorId);
    Page<DoctorResponseDto> getAllDoctors(Pageable pageable);
    Page<DoctorResponseDto> getDoctorsBySpeciality(String speciality, Pageable pageable);
    DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto dto);
    void deleteDoctor(Long doctorId);
}