package com.example.Plantation_system.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class OrganizationDTO {
    private Long organizationId;
    private Long ownerUserId;

    private String orgName;
    private String contactEmail;
    private String webUrl;
    private String purpose;

    private Instant createdAt;
}
