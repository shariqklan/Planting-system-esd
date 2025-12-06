package com.example.Plantation_system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.UserDTO;
import com.example.Plantation_system.entities.GenericStatus;
import com.example.Plantation_system.entities.Role;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.UserMapper;
import com.example.Plantation_system.repositories.DonationRepository;
import com.example.Plantation_system.repositories.MeetupRepository;
import com.example.Plantation_system.repositories.OrganizationRepository;
import com.example.Plantation_system.repositories.UserRepository;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Get all users in the system (Admin Only)
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID (Admin Only)
     */
    public UserDTO getUserById(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return userMapper.toDTO(user);
    }

    /**
     * Update user role (Admin Only)
     */
    public UserDTO updateUserRole(Long userId, String newRole) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        try {
            Role role = Role.valueOf(newRole.toUpperCase());
            user.setRole(role);
            User updatedUser = userRepository.save(user);
            return userMapper.toDTO(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid role: " + newRole + ". Valid roles: VOLUNTEER, ORG_ADMIN, ADMIN");
        }
    }

    /**
     * Update user status (Admin Only)
     */
    public UserDTO updateUserStatus(Long userId, String newStatus) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Validate and convert status
        try {
            GenericStatus status = GenericStatus.valueOf(newStatus.toUpperCase());
            user.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid status: " + newStatus + ". Valid statuses: ACTIVE, BLOCKED, PENDING, DELETED, SUSPENDED, VERIFIED");
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Delete any user from system (Admin Only)
     */
    public void deleteUserAdmin(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Prevent deletion of admin accounts by other admins (optional security measure)
        if (user.getRole() == Role.ADMIN && userId != 1) {
            throw new Exception("Cannot delete other ADMIN users. Only system super-admin can do this.");
        }

        userRepository.delete(user);
    }

    /**
     * Get total user count
     */
    public Long getTotalUserCount() {
        return userRepository.count();
    }

    /**
     * Get user count by role
     */
    public Map<String, Long> getUserCountByRole() {
        List<User> users = userRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("VOLUNTEER", users.stream().filter(u -> u.getRole() == Role.VOLUNTEER).count());
        stats.put("ORG_ADMIN", users.stream().filter(u -> u.getRole() == Role.ORG_ADMIN).count());
        stats.put("ADMIN", users.stream().filter(u -> u.getRole() == Role.ADMIN).count());
        stats.put("TOTAL", (long) users.size());
        
        return stats;
    }

    /**
     * Get total organization count
     */
    public Long getTotalOrganizationCount() {
        return organizationRepository.count();
    }

    /**
     * Get total meetup count
     */
    public Long getTotalMeetupCount() {
        return meetupRepository.count();
    }

    /**
     * Get donation statistics
     */
    public Map<String, Object> getDonationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Long totalDonations = donationRepository.count();
        
        stats.put("totalDonations", totalDonations);
        stats.put("approvedDonations", donationRepository.findByStatus("APPROVED").size());
        stats.put("pendingDonations", donationRepository.findByStatus("PENDING").size());
        stats.put("rejectedDonations", donationRepository.findByStatus("REJECTED").size());
        
        return stats;
    }

    /**
     * Get comprehensive dashboard statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // User statistics
        List<User> users = userRepository.findAll();
        dashboard.put("totalUsers", (long) users.size());
        dashboard.put("usersByRole", getUserCountByRole());
        dashboard.put("activeUsers", users.stream().filter(u -> GenericStatus.ACTIVE.equals(u.getStatus())).count());
        dashboard.put("inactiveUsers", users.stream().filter(u -> GenericStatus.PENDING.equals(u.getStatus())).count());
        dashboard.put("suspendedUsers", users.stream().filter(u -> GenericStatus.SUSPENDED.equals(u.getStatus())).count());
        
        // Organization statistics
        dashboard.put("totalOrganizations", organizationRepository.count());
        
        // Meetup statistics
        dashboard.put("totalMeetups", meetupRepository.count());
        
        // Donation statistics
        dashboard.put("donationStats", getDonationStats());
        
        // System info
        dashboard.put("systemTime", System.currentTimeMillis());
        dashboard.put("systemStatus", "OPERATIONAL");
        
        return dashboard;
    }
}
