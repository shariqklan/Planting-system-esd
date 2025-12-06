package com.example.Plantation_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStatus(String status);

    List<Payment> findByDonationDonationId(Long donationId);

    @Query("SELECT p FROM Payment p WHERE p.providerRef = :ref")
    Optional<Payment> findByTransactionRef(@Param("ref") String transactionRef);

    @Query("SELECT p FROM Payment p WHERE p.donation.donor.userId = :userId")
    List<Payment> findPaymentsByUserId(@Param("userId") Long userId);
}
