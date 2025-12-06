package com.example.Plantation_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUserId(Long userId);

    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId AND n.status = :status")
    List<Notification> findByUserAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.userId = :userId AND n.readAt IS NULL")
    Integer countUnreadNotifications(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId ORDER BY n.createdAt DESC")
    List<Notification> findUserNotificationsSorted(@Param("userId") Long userId);
}