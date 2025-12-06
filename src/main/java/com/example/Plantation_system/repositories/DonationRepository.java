package com.example.Plantation_system.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.Donation;
import com.example.Plantation_system.entities.DonationTargetType;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonorUserId(Long donorUserId);

    List<Donation> findByTargetType(DonationTargetType targetType);

    List<Donation> findByTargetId(Long targetId);

    List<Donation> findByStatus(String status);

    @Query("SELECT SUM(d.amountDonated) FROM Donation d WHERE d.targetId = :targetId AND d.targetType = :targetType AND d.status = 'COMPLETED'")
    BigDecimal getTotalDonationsByTarget(@Param("targetId") Long targetId, @Param("targetType") DonationTargetType targetType);

    @Query("SELECT d FROM Donation d WHERE d.idempotencyKey = :key")
    Optional<Donation> findByIdempotencyKey(@Param("key") String idempotencyKey);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.targetId = :targetId AND d.targetType = :targetType AND d.status = 'COMPLETED'")
    Integer countDonationsByTarget(@Param("targetId") Long targetId, @Param("targetType") DonationTargetType targetType);
}
