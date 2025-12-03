package com.example.Plantation_system.dto;

import com.example.Plantation_system.entities.DonationTargetType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class DonationDTO {
    private Long donationId;
    private BigDecimal amountDonated;
    private String currency;

    private Instant donationDate;
    private Long tiersId;

    private Long donorUserId;
    private DonationTargetType targetType;

    private Long targetId;
    private Long targetMeetupId;
    private Long targetOrganizationId;

    private String status;
    private String message;
    private String idempotencyKey;
    private String paymentRef;
}
