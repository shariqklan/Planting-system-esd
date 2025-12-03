package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
