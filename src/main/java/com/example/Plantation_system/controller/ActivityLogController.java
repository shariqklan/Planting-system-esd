package com.example.Plantation_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.ActivityLogDTO;
import com.example.Plantation_system.service.ActivityLogService;

@RestController
@RequestMapping("/api/activity-logs")
@CrossOrigin(origins = "*")
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserActivityLog(@PathVariable Long userId) {
        try {
            List<ActivityLogDTO> logs = activityLogService.getUserActivityLog(userId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/action/{actionType}")
    public ResponseEntity<?> getActivityByActionType(@PathVariable String actionType) {
        try {
            List<ActivityLogDTO> logs = activityLogService.getActivityByActionType(actionType);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<?> getActivityByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        try {
            List<ActivityLogDTO> logs = activityLogService.getActivityByEntity(entityType, entityId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllActivityLogs() {
        try {
            List<ActivityLogDTO> logs = activityLogService.getAllActivityLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
