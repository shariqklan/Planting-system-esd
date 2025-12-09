package com.example.Plantation_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.OrganizationDTO;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.OrganizationService;
import com.example.Plantation_system.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "*")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createOrganization(
            @Valid @RequestBody OrganizationDTO organizationDTO,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            OrganizationDTO createdOrg = organizationService.createOrganization(userId, organizationDTO);
            activityLogService.logOrganizationCreation(userId, createdOrg.getOrganizationId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long organizationId) {
        try {
            OrganizationDTO organization = organizationService.getOrganizationById(organizationId);
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrganizations() {
        try {
            List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<?> getOrganizationsByOwner(@PathVariable Long userId) {
        try {
            List<OrganizationDTO> organizations = organizationService.getOrganizationsByOwner(userId);
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchOrganizations(@RequestParam String name) {
        try {
            List<OrganizationDTO> organizations = organizationService.searchOrganizationsByName(name);
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<?> updateOrganization(
            @PathVariable Long organizationId,
            @Valid @RequestBody OrganizationDTO organizationDTO,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            OrganizationDTO updatedOrg = organizationService.updateOrganization(organizationId, userId, organizationDTO);
            activityLogService.logActivity(userId, "UPDATE", "ORGANIZATION", organizationId, "Updated organization");
            return ResponseEntity.ok(updatedOrg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{organizationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<?> deleteOrganization(
            @PathVariable Long organizationId,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            organizationService.deleteOrganization(organizationId, userId);
            activityLogService.logActivity(userId, "DELETE", "ORGANIZATION", organizationId, "Deleted organization");
            return ResponseEntity.ok(Map.of("message", "Organization deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{organizationId}/meetup-count")
    public ResponseEntity<?> getOrganizationMeetupCount(@PathVariable Long organizationId) {
        try {
            Integer count = organizationService.getOrganizationMeetupCount(organizationId);
            return ResponseEntity.ok(Map.of("organizationId", organizationId, "meetupCount", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new com.example.Plantation_system.exceptions.UnauthorizedException("Not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }

        if (username == null) {
            throw new com.example.Plantation_system.exceptions.UnauthorizedException("Unable to extract username from authentication");
        }

        final String uname = username;
        com.example.Plantation_system.entities.User user = userRepository.findByUsername(uname)
            .orElseThrow(() -> new com.example.Plantation_system.exceptions.ResourceNotFoundException("User not found: " + uname));

        return user.getUserId();
    }
}
