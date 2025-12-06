package com.example.Plantation_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.VolunteerProfile;

@Repository
public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {

    Optional<VolunteerProfile> findByUserUserId(Long userId);

    @Query("SELECT vp FROM VolunteerProfile vp WHERE vp.rating >= :minRating ORDER BY vp.rating DESC")
    List<VolunteerProfile> findTopVolunteersByRating(@Param("minRating") Double minRating);

    @Query("SELECT vp FROM VolunteerProfile vp ORDER BY vp.totalHours DESC")
    List<VolunteerProfile> findVolunteersByMostHours();

    @Query("SELECT vp FROM VolunteerProfile vp WHERE vp.joinedMeetupsCount > 0 ORDER BY vp.joinedMeetupsCount DESC")
    List<VolunteerProfile> findMostActiveVolunteers();
}