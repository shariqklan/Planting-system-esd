package com.example.Plantation_system.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class ActivityLogDTO {
    private Long logId;
    private Long userId;

    private String actionType;
    private String entityType;
    private Long entityId;

    private String detail;
    private String ipAddress;
    private String userAgent;

    private Instant createdAt;
}