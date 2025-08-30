package com.clearskinai.adminservice.service.impl;

import com.clearskinai.adminservice.api.dto.AppointmentAdminRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentRequestDto;
import com.clearskinai.adminservice.api.dto.AppointmentResponseDto;
import com.clearskinai.adminservice.client.clientDtos.UserDto;
import com.clearskinai.adminservice.client.feignClients.UserClient;
import com.clearskinai.adminservice.entity.Appointment;
import com.clearskinai.adminservice.entity.Doctor;
import com.clearskinai.adminservice.enums.AppointmentStatus;
import com.clearskinai.adminservice.exception.appointments.*;
import com.clearskinai.adminservice.exception.doctor.DoctorNotFoundException;
import com.clearskinai.adminservice.repository.AppointmentRepository;
import com.clearskinai.adminservice.repository.DoctorRepository;
import com.clearskinai.adminservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserClient userServiceClient;

    @Override
    public AppointmentResponseDto createAppointmentByUser(AppointmentRequestDto dto, String authenticatedEmail) {
        // 1. Validate user via UserService
        UserDto user = userServiceClient.getUserByEmail(authenticatedEmail);
        if (user == null || user.getUserId() == null) {
            throw new UserNotFoundException("User not found with email: " + authenticatedEmail);
        }

        // 2. Check if user has existing pending/rescheduled/confirmed appointments
        List<Appointment> existingAppointments = appointmentRepository.findByUserIdAndStatusIn(
                user.getUserId(),
                List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED, AppointmentStatus.RESCHEDULED)
        );

        if (!existingAppointments.isEmpty()) {
            throw new ExistingAppointmentException(
                    "Complete your previous appointments before creating a new one"
            );
        }

        // 3. Validate doctor exists
        Optional<Doctor> doctor = doctorRepository.findByName(dto.getDoctorName());
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with name: " + dto.getDoctorName());
        }

        // 4. Create Appointment
        Appointment appointment = new Appointment();
        appointment.setUserId(user.getUserId());
        appointment.setUserEmail(authenticatedEmail); // always from JWT
        appointment.setUserName(dto.getUserName());
        appointment.setDate(dto.getDate());
        appointment.setDoctorId(doctor.get().getDoctorId());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);

        // 5. Convert to Response DTO
        return mapToDto(saved, doctor.get().getName());
    }


    @Override
    public AppointmentResponseDto createAppointmentByAdmin(AppointmentAdminRequestDto dto) {
        // 1. Validate user exists via User Service
        UserDto user = userServiceClient.getUserByEmail(dto.getUserEmail());
        if (user == null || user.getUserId() == null) {
            throw new UserNotFoundException("User not found with email: " + dto.getUserEmail());
        }

        // 2. Check if user has existing pending/rescheduled/confirmed appointments
        List<Appointment> existingAppointments = appointmentRepository.findByUserIdAndStatusIn(
                user.getUserId(),
                List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED, AppointmentStatus.RESCHEDULED)
        );

        if (!existingAppointments.isEmpty()) {
            throw new ExistingAppointmentException(
                    "This user already has an appointment. Try to add a new one after completing it"
            );
        }

        // 3. Validate doctor exists
        Optional<Doctor> doctor = doctorRepository.findByName(dto.getDoctorName());
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with name: " + dto.getDoctorName());
        }

        // 4. Create Appointment
        Appointment appointment = new Appointment();
        appointment.setUserId(user.getUserId());
        appointment.setUserEmail(dto.getUserEmail());
        appointment.setUserName(dto.getUserName());
        appointment.setDate(dto.getDate());
        appointment.setDoctorId(doctor.get().getDoctorId());
        appointment.setStatus(dto.getStatus());

        Appointment saved = appointmentRepository.save(appointment);

        // 5. Convert to Response DTO
        return mapToDto(saved, doctor.get().getName());
    }


    @Override
    public List<AppointmentResponseDto> getAppointmentsByUserId(Long userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return appointments.stream()
                .map(appointment -> {
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
                    return mapToDto(appointment, doctor.map(Doctor::getName).orElse("Unknown"));
                })
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));
        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
        return mapToDto(appointment, doctor.map(Doctor::getName).orElse("Unknown"));
    }

    @Override
    public Page<AppointmentResponseDto> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        List<AppointmentResponseDto> dtos = appointments.stream()
                .map(appointment -> {
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
                    return mapToDto(appointment, doctor.map(Doctor::getName).orElse("Unknown"));
                })
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, appointments.getTotalElements());
    }

    @Override
    public AppointmentResponseDto updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
        return mapToDto(updated, doctor.map(Doctor::getName).orElse("Unknown"));
    }


    @Override
    public Page<AppointmentResponseDto> searchAppointments(String userName, String userEmail, AppointmentStatus status, LocalDateTime date, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.searchAppointments(userName, userEmail, status, date, pageable);
        List<AppointmentResponseDto> dtos = appointments.stream()
                .map(appointment -> {
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
                    return mapToDto(appointment, doctor.map(Doctor::getName).orElse("Unknown"));
                })
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, appointments.getTotalElements());
    }

    @Override
    public Page<AppointmentResponseDto> searchUserAppointments(Long userId, AppointmentStatus status, LocalDateTime date, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.searchUserAppointments(userId, status, date, pageable);
        List<AppointmentResponseDto> dtos = appointments.stream()
                .map(appointment -> {
                    Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
                    return mapToDto(appointment, doctor.map(Doctor::getName).orElse("Unknown"));
                })
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, appointments.getTotalElements());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userServiceClient.getUserByEmail(email);
    }

    @Override
    public AppointmentResponseDto updateAppointmentByUser(Long appointmentId, Long userId, AppointmentRequestDto dto, String authenticatedEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        // Validate the user owns this appointment
        if (!appointment.getUserId().equals(userId)) {
            throw new CannotUpdateAppointmentException("Users can only update their own appointments");
        }
        if (!appointment.getUserEmail().equals(authenticatedEmail)) {
            throw new CannotUpdateAppointmentException("Users can only update their own appointments");
        }

        // Only PENDING appointments can be updated
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException("You can only update pending appointments");
        }

        Optional<Doctor> doctor = doctorRepository.findByName(dto.getDoctorName());
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with name: " + dto.getDoctorName());
        }

        appointment.setDate(dto.getDate());
        appointment.setDoctorId(doctor.get().getDoctorId());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated, doctor.get().getName());
    }

    @Override
    public void deleteAppointmentByUser(Long appointmentId, Long userId, String authenticatedEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        if (!appointment.getUserId().equals(userId) || !appointment.getUserEmail().equals(authenticatedEmail)) {
            throw new IllegalArgumentException("Users can only delete their own appointments");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException("You can only delete pending appointments");
        }

        appointmentRepository.delete(appointment);
    }

    @Override
    public AppointmentResponseDto updateAppointmentByAdminStaff(Long appointmentId, AppointmentRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        // Admin/Staff can only update PENDING appointments
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException("Only pending appointments can be updated");
        }

        Optional<Doctor> doctor = doctorRepository.findByName(dto.getDoctorName());
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with name: " + dto.getDoctorName());
        }

        appointment.setDate(dto.getDate());
        appointment.setDoctorId(doctor.get().getDoctorId());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated, doctor.get().getName());
    }

    @Override
    public void deleteAppointmentByAdminStaff(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        // Admin/Staff can delete any appointment
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponseDto mapToDto(Appointment appointment, String doctorName) {
        return new AppointmentResponseDto(
                appointment.getAppointmentId(),
                appointment.getUserId(),
                appointment.getUserEmail(),
                appointment.getUserName(),
                appointment.getDate(),
                appointment.getDoctorId(),
                doctorName,
                appointment.getStatus()
        );
    }
}
