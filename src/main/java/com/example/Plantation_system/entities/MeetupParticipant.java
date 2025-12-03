package com.example.Plantation_system.entities;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "meetup_participants",
       uniqueConstraints = @UniqueConstraint(columnNames = {"meetup_id","participant_id"}))
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)

public class MeetupParticipant {
    @EmbeddedId
    private MeetupParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("meetupId")
    @JoinColumn(name = "meetup_id")
    private Meetup meetup;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private VolunteerProfile participant; // changed from User to VolunteerProfile

    private String status = "JOINED";

    @CreationTimestamp
    private Instant joinedAt;

    private Instant attendedAt;

    private BigDecimal hoursContributed;

    @Column(columnDefinition = "text")
    private String note;
}
