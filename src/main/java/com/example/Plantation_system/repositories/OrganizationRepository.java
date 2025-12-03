package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
