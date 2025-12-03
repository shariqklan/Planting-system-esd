package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.MeetupParticipant;
import com.example.Plantation_system.entities.MeetupParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupParticipantRepository extends JpaRepository<MeetupParticipant, MeetupParticipantId> {
}
