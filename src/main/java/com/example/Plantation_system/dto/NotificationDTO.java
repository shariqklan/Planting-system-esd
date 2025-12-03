package com.example.Plantation_system.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class NotificationDTO {
    private Long notificationId;
    private Long userId;

    private String type;
    private String channel;
    private String subject;
    private String body;
    private String payload;
    private String status;

    private Instant sentAt;
    private Instant deliveredAt;
    private Instant readAt;

    private Instant createdAt;
}