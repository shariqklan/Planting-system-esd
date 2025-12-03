package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
}