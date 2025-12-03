package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}