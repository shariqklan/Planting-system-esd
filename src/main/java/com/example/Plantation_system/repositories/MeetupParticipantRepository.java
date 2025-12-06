package com.example.Plantation_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.MeetupParticipant;
import com.example.Plantation_system.entities.MeetupParticipantId;

@Repository
public interface MeetupParticipantRepository extends JpaRepository<MeetupParticipant, MeetupParticipantId> {

    @Query("SELECT mp FROM MeetupParticipant mp WHERE mp.meetup.meetupId = :meetupId")
    List<MeetupParticipant> findByMeetupId(@Param("meetupId") Long meetupId);

    @Query("SELECT mp FROM MeetupParticipant mp WHERE mp.participant.profileId = :participantId")
    List<MeetupParticipant> findByParticipantId(@Param("participantId") Long participantId);

    @Query("SELECT mp FROM MeetupParticipant mp WHERE mp.meetup.meetupId = :meetupId AND mp.status = :status")
    List<MeetupParticipant> findByMeetupAndStatus(@Param("meetupId") Long meetupId, @Param("status") String status);

    @Query("SELECT CASE WHEN COUNT(mp) > 0 THEN true ELSE false END FROM MeetupParticipant mp WHERE mp.meetup.meetupId = :meetupId AND mp.participant.profileId = :participantId")
    boolean isUserParticipantInMeetup(@Param("meetupId") Long meetupId, @Param("participantId") Long participantId);
}
