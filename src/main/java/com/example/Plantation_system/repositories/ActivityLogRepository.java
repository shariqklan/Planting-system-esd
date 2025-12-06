package com.example.Plantation_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Plantation_system.entities.ActivityLog;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByUserUserId(Long userId);

    List<ActivityLog> findByActionType(String actionType);

    @Query("SELECT al FROM ActivityLog al WHERE al.user.userId = :userId ORDER BY al.createdAt DESC")
    List<ActivityLog> findUserActivityLog(@Param("userId") Long userId);

    @Query("SELECT al FROM ActivityLog al WHERE al.entityType = :entityType AND al.entityId = :entityId")
    List<ActivityLog> findActivityByEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId);
}
