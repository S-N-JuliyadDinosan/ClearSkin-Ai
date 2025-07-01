package com.clearskin_ai.adminservice.entity;

import com.clearskin_ai.adminservice.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Staff {
    @Id
    private Long staffId;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
