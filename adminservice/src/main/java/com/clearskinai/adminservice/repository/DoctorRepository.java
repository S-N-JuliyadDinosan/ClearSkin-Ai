package com.clearskinai.adminservice.repository;

import com.clearskinai.adminservice.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findBySpeciality(String speciality, Pageable pageable);
    Optional<Doctor> findByNameAndQualifications(String name, String qualifications);
}