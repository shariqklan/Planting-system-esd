package com.example.Plantation_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.Plantation_system.dto.ActivityLogDTO;
import com.example.Plantation_system.entities.ActivityLog;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.ActivityLogMapper;
import com.example.Plantation_system.repositories.ActivityLogRepository;
import com.example.Plantation_system.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityLogMapper activityLogMapper;

    public ActivityLogDTO logActivity(Long userId, String actionType, String entityType, Long entityId, String detail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetail(detail);
        
        // Try to get request details if available
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                log.setIpAddress(getClientIpAddress(request));
                log.setUserAgent(request.getHeader("User-Agent"));
            }
        } catch (Exception e) {
            // Request context not available, skip setting request details
        }

        ActivityLog savedLog = activityLogRepository.save(log);
        return activityLogMapper.toDTO(savedLog);
    }

    public List<ActivityLogDTO> getUserActivityLog(Long userId) {
        return activityLogRepository.findUserActivityLog(userId).stream()
                .map(activityLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityLogDTO> getActivityByActionType(String actionType) {
        return activityLogRepository.findByActionType(actionType).stream()
                .map(activityLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityLogDTO> getActivityByEntity(String entityType, Long entityId) {
        return activityLogRepository.findActivityByEntity(entityType, entityId).stream()
                .map(activityLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityLogDTO> getAllActivityLogs() {
        return activityLogRepository.findAll().stream()
                .map(activityLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convenience methods for common activities
    public void logMeetupCreation(Long userId, Long meetupId) {
        logActivity(userId, "CREATE", "MEETUP", meetupId, "Created new meetup");
    }

    public void logMeetupJoin(Long userId, Long meetupId) {
        logActivity(userId, "JOIN", "MEETUP", meetupId, "Joined meetup");
    }

    public void logMeetupLeave(Long userId, Long meetupId) {
        logActivity(userId, "LEAVE", "MEETUP", meetupId, "Left meetup");
    }

    public void logDonation(Long userId, Long donationId) {
        logActivity(userId, "CREATE", "DONATION", donationId, "Made donation");
    }

    public void logOrganizationCreation(Long userId, Long organizationId) {
        logActivity(userId, "CREATE", "ORGANIZATION", organizationId, "Created organization");
    }

    public void logVolunteerProfileCreation(Long userId, Long profileId) {
        logActivity(userId, "CREATE", "VOLUNTEER_PROFILE", profileId, "Created volunteer profile");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
