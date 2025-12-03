package com.example.Plantation_system.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class OrganizationUserDTO {
    private Long id;
    private Long userId;
    private Long organizationId;

    private String orgRole;
    private Boolean isPrimary;
    private Instant joinedAt;
}
