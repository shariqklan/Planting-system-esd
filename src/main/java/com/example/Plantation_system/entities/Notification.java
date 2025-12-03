package com.example.Plantation_system.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type;

    private String channel;

    private String subject;

    @Column(columnDefinition = "text")
    private String body;

    @Column(columnDefinition = "text")
    private String payload;

    private String status = "QUEUED";

    private Instant sentAt;

    private Instant deliveredAt;

    private Instant readAt;

    @CreationTimestamp
    private Instant createdAt;
}
