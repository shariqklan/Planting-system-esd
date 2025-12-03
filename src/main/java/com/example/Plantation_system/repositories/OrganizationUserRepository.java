package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.OrganizationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {
}
