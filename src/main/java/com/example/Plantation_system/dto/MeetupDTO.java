package com.example.Plantation_system.dto;

import lombok.Data;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;

@Data
public class MeetupDTO {
    private Long meetupId;
    private String meetupName;

    private Date meetupDate;
    private Time startTime;
    private String duration;
    private String description;

    private Long hostUserId;
    private Long organizationId;

    private Integer capacity;
    private String status;

    private Instant createdAt;
    private Instant updatedAt;
}