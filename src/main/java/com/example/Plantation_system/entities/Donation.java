package com.example.Plantation_system.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donations", indexes = {
    @Index(name = "idx_donation_idempotency", columnList = "idempotencyKey")
})
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)

public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    private BigDecimal amountDonated;

    private String currency = "PKR";

    @CreationTimestamp
    private Instant donationDate;

    private Long tiersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private User donor;

    @Enumerated(EnumType.STRING)
    private DonationTargetType targetType;

    // targetId used to reference either an org or meetup depending on targetType
    private Long targetId;

    // optional convenience relation (read-only) to meetup
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_meetup_id", insertable = false, updatable = false)
    private Meetup targetMeetup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_organization_id", insertable = false, updatable = false)
    private Organization targetOrganization;

    private String status = "PENDING";

    private String message;

    @Column(unique = true)
    private String idempotencyKey;

    private String paymentRef;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();
}
