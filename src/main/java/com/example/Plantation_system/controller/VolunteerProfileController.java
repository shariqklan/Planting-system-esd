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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.Plantation_system.dto.VolunteerProfileDTO;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.VolunteerProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/volunteer-profiles")
@CrossOrigin(origins = "*")
public class VolunteerProfileController {

    @Autowired
    private VolunteerProfileService volunteerProfileService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private com.example.Plantation_system.security.JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createVolunteerProfile(
            @Valid @RequestBody VolunteerProfileDTO profileDTO,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            VolunteerProfileDTO createdProfile = volunteerProfileService.createVolunteerProfile(userId, profileDTO);
            activityLogService.logVolunteerProfileCreation(userId, createdProfile.getProfileId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<?> getVolunteerProfileById(@PathVariable Long profileId) {
        try {
            VolunteerProfileDTO profile = volunteerProfileService.getVolunteerProfileById(profileId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getVolunteerProfileByUserId(@PathVariable Long userId) {
        try {
            VolunteerProfileDTO profile = volunteerProfileService.getVolunteerProfileByUserId(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllVolunteerProfiles() {
        try {
            List<VolunteerProfileDTO> profiles = volunteerProfileService.getAllVolunteerProfiles();
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<?> updateVolunteerProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody VolunteerProfileDTO profileDTO) {
        try {
            VolunteerProfileDTO updatedProfile = volunteerProfileService.updateVolunteerProfile(profileId, profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{profileId}/skills")
    public ResponseEntity<?> addSkill(
            @PathVariable Long profileId,
            @RequestBody Map<String, String> request) {
        try {
            String skill = request.get("skill");
            volunteerProfileService.addSkill(profileId, skill);
            return ResponseEntity.ok(Map.of("message", "Skill added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{profileId}/skills")
    public ResponseEntity<?> removeSkill(
            @PathVariable Long profileId,
            @RequestBody Map<String, String> request) {
        try {
            String skill = request.get("skill");
            volunteerProfileService.removeSkill(profileId, skill);
            return ResponseEntity.ok(Map.of("message", "Skill removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{profileId}/hours")
    public ResponseEntity<?> updateTotalHours(
            @PathVariable Long profileId,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal hours = request.get("hours");
            volunteerProfileService.updateTotalHours(profileId, hours);
            return ResponseEntity.ok(Map.of("message", "Hours updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{profileId}/rating")
    public ResponseEntity<?> updateRating(
            @PathVariable Long profileId,
            @RequestBody Map<String, Double> request) {
        try {
            Double rating = request.get("rating");
            volunteerProfileService.updateRating(profileId, rating);
            return ResponseEntity.ok(Map.of("message", "Rating updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopVolunteersByRating(@RequestParam Double minRating) {
        try {
            List<VolunteerProfileDTO> profiles = volunteerProfileService.getTopVolunteersByRating(minRating);
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/most-active")
    public ResponseEntity<?> getMostActiveVolunteers() {
        try {
            List<VolunteerProfileDTO> profiles = volunteerProfileService.getMostActiveVolunteers();
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<?> deleteVolunteerProfile(@PathVariable Long profileId) {
        try {
            volunteerProfileService.deleteVolunteerProfile(profileId);
            return ResponseEntity.ok(Map.of("message", "Volunteer profile deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        String token = extractTokenFromRequest();
        if (token != null && !token.isEmpty()) {
            try {
                return jwtUtil.extractUserId(token);
            } catch (Exception e) {
                throw new RuntimeException("Could not extract userId from token: " + e.getMessage());
            }
        }
        throw new RuntimeException("No JWT token found in request");
    }

    private String extractTokenFromRequest() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String header = request.getHeader("Authorization");
                if (header != null && header.startsWith("Bearer ")) {
                    return header.substring(7);
                }
            }
        } catch (Exception e) {
            // Log and continue
        }
        return null;
    }
}
