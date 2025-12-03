package com.example.Plantation_system.repositories;

import com.example.Plantation_system.entities.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
}
