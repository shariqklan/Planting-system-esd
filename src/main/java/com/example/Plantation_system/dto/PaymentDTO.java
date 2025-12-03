package com.example.Plantation_system.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Long donationId;

    private String method;
    private String provider;
    private String providerRef;

    private String status;
    private BigDecimal amount;
    private String currency;

    private String rawResponse;
    private Instant paidAt;
    private Instant createdAt;
}