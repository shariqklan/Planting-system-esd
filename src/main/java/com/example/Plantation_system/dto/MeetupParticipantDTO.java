package com.example.Plantation_system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MeetupParticipantDTO {
    private Long meetupId;
    private Long participantId;

    private String status;
    private Instant joinedAt;
    private Instant attendedAt;
    private BigDecimal hoursContributed;
    private String note;
}