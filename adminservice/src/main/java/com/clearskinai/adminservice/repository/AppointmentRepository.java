package com.clearskinai.adminservice.repository;

import com.clearskinai.adminservice.entity.Appointment;
import com.clearskinai.adminservice.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserId(Long userId);

    @Query("SELECT a FROM Appointment a WHERE " +
            "(:userName IS NULL OR a.userName LIKE %:userName%) AND " +
            "(:userEmail IS NULL OR a.userEmail = :userEmail) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:date IS NULL OR a.date = :date)")
    Page<Appointment> searchAppointments(
            @Param("userName") String userName,
            @Param("userEmail") String userEmail,
            @Param("status") AppointmentStatus status,
            @Param("date") LocalDateTime date,
            Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.userId = :userId AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:date IS NULL OR a.date = :date)")
    Page<Appointment> searchUserAppointments(
            @Param("userId") Long userId,
            @Param("status") AppointmentStatus status,
            @Param("date") LocalDateTime date,
            Pageable pageable);

    List<Appointment> findByUserIdAndStatusIn(Long userId, List<AppointmentStatus> statuses);

}
