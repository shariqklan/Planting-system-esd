package com.example.Plantation_system.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.MeetupDTO;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.MeetupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meetups")
@CrossOrigin(origins = "*")
public class MeetupController {

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping
    public ResponseEntity<?> createMeetup(@Valid @RequestBody MeetupDTO meetupDTO, Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            MeetupDTO createdMeetup = meetupService.createMeetup(userId, meetupDTO);
            activityLogService.logMeetupCreation(userId, createdMeetup.getMeetupId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMeetup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMeetups() {
        try {
            List<MeetupDTO> meetups = meetupService.getAllMeetups();
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{meetupId}")
    public ResponseEntity<?> getMeetupById(@PathVariable Long meetupId) {
        try {
            MeetupDTO meetup = meetupService.getMeetupById(meetupId);
            return ResponseEntity.ok(meetup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<?> getMeetupsByOrganization(@PathVariable Long organizationId) {
        try {
            List<MeetupDTO> meetups = meetupService.getMeetupsByOrganization(organizationId);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/host/{hostUserId}")
    public ResponseEntity<?> getMeetupsByHost(@PathVariable Long hostUserId) {
        try {
            List<MeetupDTO> meetups = meetupService.getMeetupsByHost(hostUserId);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getMeetupsByStatus(@PathVariable String status) {
        try {
            List<MeetupDTO> meetups = meetupService.getMeetupsByStatus(status);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/upcoming/{organizationId}")
    public ResponseEntity<?> getUpcomingMeetups(@PathVariable Long organizationId) {
        try {
            List<MeetupDTO> meetups = meetupService.getUpcomingMeetups(organizationId);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> getMeetupsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            List<MeetupDTO> meetups = meetupService.getMeetupsByDateRange(startDate, endDate);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{meetupId}")
    public ResponseEntity<?> updateMeetup(
            @PathVariable Long meetupId,
            @Valid @RequestBody MeetupDTO meetupDTO,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            MeetupDTO updatedMeetup = meetupService.updateMeetup(meetupId, userId, meetupDTO);
            activityLogService.logActivity(userId, "UPDATE", "MEETUP", meetupId, "Updated meetup");
            return ResponseEntity.ok(updatedMeetup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{meetupId}")
    public ResponseEntity<?> deleteMeetup(
            @PathVariable Long meetupId,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            meetupService.deleteMeetup(meetupId, userId);
            activityLogService.logActivity(userId, "DELETE", "MEETUP", meetupId, "Deleted meetup");
            return ResponseEntity.ok(Map.of("message", "Meetup deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{meetupId}/status")
    public ResponseEntity<?> updateMeetupStatus(
            @PathVariable Long meetupId,
            @RequestParam String status,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            meetupService.updateMeetupStatus(meetupId, status);
            activityLogService.logActivity(userId, "UPDATE_STATUS", "MEETUP", meetupId, "Updated status to " + status);
            return ResponseEntity.ok(Map.of("message", "Meetup status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{meetupId}/participant-count")
    public ResponseEntity<?> getParticipantCount(@PathVariable Long meetupId) {
        try {
            Integer count = meetupService.getParticipantCount(meetupId);
            Map<String, Object> response = new HashMap<>();
            response.put("meetupId", meetupId);
            response.put("participantCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        // Extract user ID from JWT token or user details
        // This assumes the user ID is available in the authentication principal
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            // For now, we'll need to implement a way to get user ID from username
            // This is a simplified implementation
            return 1L; // Placeholder - should be extracted from token
        }
        return 1L;
    }
}