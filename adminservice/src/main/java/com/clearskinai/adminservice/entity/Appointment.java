package com.clearskinai.adminservice.entity;

import com.clearskinai.adminservice.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private Long userId;
    private String userEmail;
    private String userName;
    private LocalDateTime date;
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


}
