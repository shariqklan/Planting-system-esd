package com.example.Plantation_system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class VolunteerProfileDTO {
    private Long profileId;
    private Long userId;
    private String bio;
    private String[] skills;
    private BigDecimal totalHours;
    private BigDecimal rating;
    private Integer joinedMeetupsCount;

    private Instant createdAt;
    private Instant updatedAt;
}