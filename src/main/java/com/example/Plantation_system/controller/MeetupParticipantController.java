package com.example.Plantation_system.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.MeetupParticipantDTO;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.MeetupParticipantService;

@RestController
@RequestMapping("/api/meetup-participants")
@CrossOrigin(origins = "*")
public class MeetupParticipantController {

    @Autowired
    private MeetupParticipantService meetupParticipantService;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping("/{meetupId}/join/{participantId}")
    public ResponseEntity<?> joinMeetup(
            @PathVariable Long meetupId,
            @PathVariable Long participantId,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            MeetupParticipantDTO participant = meetupParticipantService.joinMeetup(meetupId, participantId);
            activityLogService.logMeetupJoin(userId, meetupId);
            return ResponseEntity.status(HttpStatus.CREATED).body(participant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{meetupId}/leave/{participantId}")
    public ResponseEntity<?> leaveMeetup(
            @PathVariable Long meetupId,
            @PathVariable Long participantId,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            meetupParticipantService.leaveMeetup(meetupId, participantId);
            activityLogService.logMeetupLeave(userId, meetupId);
            return ResponseEntity.ok(Map.of("message", "Left meetup successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/meetup/{meetupId}")
    public ResponseEntity<?> getParticipantsByMeetup(@PathVariable Long meetupId) {
        try {
            List<MeetupParticipantDTO> participants = meetupParticipantService.getParticipantsByMeetup(meetupId);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<?> getMeetupsByParticipant(@PathVariable Long participantId) {
        try {
            List<MeetupParticipantDTO> meetups = meetupParticipantService.getMeetupsByParticipant(participantId);
            return ResponseEntity.ok(meetups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{meetupId}/attendance/{participantId}")
    public ResponseEntity<?> markAttendance(
            @PathVariable Long meetupId,
            @PathVariable Long participantId) {
        try {
            meetupParticipantService.markAttendance(meetupId, participantId);
            return ResponseEntity.ok(Map.of("message", "Attendance marked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{meetupId}/hours/{participantId}")
    public ResponseEntity<?> updateHoursContributed(
            @PathVariable Long meetupId,
            @PathVariable Long participantId,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal hours = request.get("hours");
            meetupParticipantService.updateHoursContributed(meetupId, participantId, hours);
            return ResponseEntity.ok(Map.of("message", "Hours updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{meetupId}/note/{participantId}")
    public ResponseEntity<?> addNote(
            @PathVariable Long meetupId,
            @PathVariable Long participantId,
            @RequestBody Map<String, String> request) {
        try {
            String note = request.get("note");
            meetupParticipantService.addNote(meetupId, participantId, note);
            return ResponseEntity.ok(Map.of("message", "Note added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{meetupId}/attendees")
    public ResponseEntity<?> getAttendeesByMeetup(@PathVariable Long meetupId) {
        try {
            List<MeetupParticipantDTO> attendees = meetupParticipantService.getAttendeesByMeetup(meetupId);
            return ResponseEntity.ok(attendees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{meetupId}/attendance-count")
    public ResponseEntity<?> getAttendanceCount(@PathVariable Long meetupId) {
        try {
            Integer count = meetupParticipantService.getAttendanceCount(meetupId);
            return ResponseEntity.ok(Map.of("meetupId", meetupId, "attendanceCount", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{meetupId}/is-joined/{participantId}")
    public ResponseEntity<?> isUserJoinedMeetup(
            @PathVariable Long meetupId,
            @PathVariable Long participantId) {
        try {
            boolean isJoined = meetupParticipantService.isUserJoinedMeetup(meetupId, participantId);
            return ResponseEntity.ok(Map.of(
                    "meetupId", meetupId,
                    "participantId", participantId,
                    "isJoined", isJoined
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        return 1L; // Placeholder - should be extracted from token
    }
}
