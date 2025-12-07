package com.example.Plantation_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.UserDTO;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ActivityLogService activityLogService;

    /**
     * Get all users in the system
     * ADMIN ONLY
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsersAdmin() {
        try {
            List<UserDTO> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user by ID
     * ADMIN ONLY
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserByIdAdmin(@PathVariable Long userId) {
        try {
            UserDTO user = adminService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update user role
     * ADMIN ONLY - Can change any user's role
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        try {
            String newRole = request.get("role");
            if (newRole == null || newRole.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Role is required"));
            }

            UserDTO updatedUser = adminService.updateUserRole(userId, newRole);
            activityLogService.logActivity(1L, "UPDATE_ROLE", "USER", userId, "Changed role to " + newRole);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update user status
     * ADMIN ONLY - Can change any user's status
     */
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("status");
            if (newStatus == null || newStatus.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Status is required"));
            }

            UserDTO updatedUser = adminService.updateUserStatus(userId, newStatus);
            activityLogService.logActivity(1L, "UPDATE_STATUS", "USER", userId, "Changed status to " + newStatus);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete any user from the system
     * ADMIN ONLY
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUserAdmin(@PathVariable Long userId) {
        try {
            adminService.deleteUserAdmin(userId);
            activityLogService.logActivity(1L, "DELETE", "USER", userId, "User deleted by admin");
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user count statistics
     * ADMIN ONLY
     */
    @GetMapping("/stats/user-count")
    public ResponseEntity<?> getUserCount() {
        try {
            Long count = adminService.getTotalUserCount();
            return ResponseEntity.ok(Map.of("totalUsers", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get user count by role
     * ADMIN ONLY
     */
    @GetMapping("/stats/users-by-role")
    public ResponseEntity<?> getUsersByRole() {
        try {
            Map<String, Long> stats = adminService.getUserCountByRole();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get organization statistics
     * ADMIN ONLY
     */
    @GetMapping("/stats/organization-count")
    public ResponseEntity<?> getOrganizationCount() {
        try {
            Long count = adminService.getTotalOrganizationCount();
            return ResponseEntity.ok(Map.of("totalOrganizations", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get meetup statistics
     * ADMIN ONLY
     */
    @GetMapping("/stats/meetup-count")
    public ResponseEntity<?> getMeetupCount() {
        try {
            Long count = adminService.getTotalMeetupCount();
            return ResponseEntity.ok(Map.of("totalMeetups", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get donation statistics
     * ADMIN ONLY
     */
    @GetMapping("/stats/donation-stats")
    public ResponseEntity<?> getDonationStats() {
        try {
            Map<String, Object> stats = adminService.getDonationStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get system overview/dashboard data
     * ADMIN ONLY
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            Map<String, Object> dashboard = adminService.getDashboardStats();
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get system health check
     * ADMIN ONLY
     */
    @GetMapping("/health")
    public ResponseEntity<?> getSystemHealth() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("timestamp", System.currentTimeMillis());
            health.put("message", "System is running normally");
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }
}
