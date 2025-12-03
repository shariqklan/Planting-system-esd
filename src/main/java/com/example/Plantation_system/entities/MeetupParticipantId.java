package com.example.Plantation_system.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MeetupParticipantId implements Serializable {
    private Long meetupId;
    private Long participantId;
}
