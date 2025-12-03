package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
