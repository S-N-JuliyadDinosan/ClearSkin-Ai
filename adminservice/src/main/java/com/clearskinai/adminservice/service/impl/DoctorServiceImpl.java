package com.clearskinai.adminservice.service.impl;

import com.clearskinai.adminservice.api.dto.DoctorRequestDto;
import com.clearskinai.adminservice.api.dto.DoctorResponseDto;
import com.clearskinai.adminservice.entity.Doctor;
import com.clearskinai.adminservice.exception.doctor.DoctorAlreadyExistsException;
import com.clearskinai.adminservice.exception.doctor.DoctorNotFoundException;
import com.clearskinai.adminservice.repository.DoctorRepository;
import com.clearskinai.adminservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto dto) {
        Optional<Doctor> existingDoctor = doctorRepository.findByNameAndQualifications(dto.getName(), dto.getQualifications());
        if (existingDoctor.isPresent()) {
            throw new DoctorAlreadyExistsException("Doctor already exists with name: " + dto.getName() + " and qualifications: " + dto.getQualifications());
        }

        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setQualifications(dto.getQualifications());
        doctor.setSpeciality(dto.getSpeciality());

        Doctor saved = doctorRepository.save(doctor);
        return mapToDto(saved);
    }

    @Override
    public DoctorResponseDto getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + doctorId));
        return mapToDto(doctor);
    }

    @Override
    public Page<DoctorResponseDto> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        List<DoctorResponseDto> dtos = doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, doctors.getTotalElements());
    }

    @Override
    public Page<DoctorResponseDto> getDoctorsBySpeciality(String speciality, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findBySpeciality(speciality, pageable);
        List<DoctorResponseDto> dtos = doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, doctors.getTotalElements());
    }

    @Override
    public DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto dto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + doctorId));

        doctor.setName(dto.getName());
        doctor.setQualifications(dto.getQualifications());
        doctor.setSpeciality(dto.getSpeciality());

        Doctor updated = doctorRepository.save(doctor);
        return mapToDto(updated);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + doctorId));
        doctorRepository.delete(doctor);
    }

    private DoctorResponseDto mapToDto(Doctor doctor) {
        return new DoctorResponseDto(
                doctor.getDoctorId(),
                doctor.getName(),
                doctor.getQualifications(),
                doctor.getSpeciality()
        );
    }
}