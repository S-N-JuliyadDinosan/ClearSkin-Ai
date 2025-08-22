package com.clearskinai.adminservice.entity;

import com.clearskinai.adminservice.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
