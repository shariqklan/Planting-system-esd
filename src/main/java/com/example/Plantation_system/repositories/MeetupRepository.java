package com.example.Plantation_system.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.Meetup;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long> {

    @Query("SELECT m FROM Meetup m WHERE m.organization.organizationId = :organizationId")
    List<Meetup> findByOrganizationId(@Param("organizationId") Long organizationId);

    List<Meetup> findByHostUserId(Long hostUserId);

    List<Meetup> findByStatus(String status);

    @Query("SELECT m FROM Meetup m WHERE m.meetupDate >= :startDate AND m.meetupDate <= :endDate")
    List<Meetup> findMeetupsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM Meetup m WHERE m.organization.organizationId = :orgId AND m.status = 'SCHEDULED'")
    List<Meetup> findUpcomingMeetupsByOrganization(@Param("orgId") Long organizationId);

    @Query("SELECT COUNT(mp) FROM MeetupParticipant mp WHERE mp.meetup.meetupId = :meetupId")
    Integer countParticipantsByMeetupId(@Param("meetupId") Long meetupId);
}