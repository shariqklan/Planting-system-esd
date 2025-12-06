package com.example.Plantation_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.OrganizationUser;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {

    @Query("SELECT ou FROM OrganizationUser ou WHERE ou.organization.organizationId = :orgId")
    List<OrganizationUser> findByOrganizationId(@Param("orgId") Long organizationId);

    @Query("SELECT ou FROM OrganizationUser ou WHERE ou.user.userId = :userId")
    List<OrganizationUser> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ou FROM OrganizationUser ou WHERE ou.organization.organizationId = :orgId AND ou.role = :role")
    List<OrganizationUser> findByOrganizationAndRole(@Param("orgId") Long organizationId, @Param("role") String role);
}
