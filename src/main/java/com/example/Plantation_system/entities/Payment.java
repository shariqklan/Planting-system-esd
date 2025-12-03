package com.example.Plantation_system.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    private String method;

    private String provider;

    private String providerRef;

    private String status = "INITIATED";

    private BigDecimal amount;

    private String currency;

    @Column(columnDefinition = "text")
    private String rawResponse;

    private Instant paidAt;

    @CreationTimestamp
    private Instant createdAt;
}
