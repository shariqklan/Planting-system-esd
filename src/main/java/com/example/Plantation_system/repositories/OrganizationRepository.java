package com.example.Plantation_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByOwnerUserId(Long ownerUserId);

    @Query("SELECT o FROM Organization o WHERE o.orgName LIKE %:name%")
    List<Organization> searchByName(@Param("name") String name);

    @Query("SELECT o FROM Organization o WHERE o.owner.userId = :userId")
    List<Organization> findOrganizationsByOwner(@Param("userId") Long userId);
}
