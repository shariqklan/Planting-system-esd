package com.example.Plantation_system.dto;

import com.example.Plantation_system.entities.*;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String phone;

    private Mode mode;
    private Role role;
    private GenericStatus status;

    private Instant createdAt;
    private Instant updatedAt;
}
