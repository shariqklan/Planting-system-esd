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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Plantation_system.dto.DonationDTO;
import com.example.Plantation_system.entities.DonationTargetType;
import com.example.Plantation_system.service.ActivityLogService;
import com.example.Plantation_system.service.DonationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping
    public ResponseEntity<?> createDonation(
            @Valid @RequestBody DonationDTO donationDTO,
            Authentication authentication) {
        try {
            Long userId = extractUserIdFromAuthentication(authentication);
            DonationDTO createdDonation = donationService.createDonation(userId, donationDTO);
            activityLogService.logDonation(userId, createdDonation.getDonationId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{donationId}")
    public ResponseEntity<?> getDonationById(@PathVariable Long donationId) {
        try {
            DonationDTO donation = donationService.getDonationById(donationId);
            return ResponseEntity.ok(donation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDonations() {
        try {
            List<DonationDTO> donations = donationService.getAllDonations();
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/donor/{donorUserId}")
    public ResponseEntity<?> getDonationsByDonor(@PathVariable Long donorUserId) {
        try {
            List<DonationDTO> donations = donationService.getDonationsByDonor(donorUserId);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getDonationsByStatus(@PathVariable String status) {
        try {
            List<DonationDTO> donations = donationService.getDonationsByStatus(status);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/target/{targetId}/{targetType}")
    public ResponseEntity<?> getDonationsByTarget(
            @PathVariable Long targetId,
            @PathVariable DonationTargetType targetType) {
        try {
            List<DonationDTO> donations = donationService.getDonationsByTarget(targetId, targetType);
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/total/{targetId}/{targetType}")
    public ResponseEntity<?> getTotalDonations(
            @PathVariable Long targetId,
            @PathVariable DonationTargetType targetType) {
        try {
            BigDecimal total = donationService.getTotalDonationsByTarget(targetId, targetType);
            Integer count = donationService.getDonationCountByTarget(targetId, targetType);
            return ResponseEntity.ok(Map.of(
                    "targetId", targetId,
                    "targetType", targetType,
                    "total", total,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{donationId}/approve")
    public ResponseEntity<?> approveDonation(
            @PathVariable Long donationId,
            Authentication authentication) {
        try {
            donationService.approveDonation(donationId);
            return ResponseEntity.ok(Map.of("message", "Donation approved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{donationId}/reject")
    public ResponseEntity<?> rejectDonation(
            @PathVariable Long donationId,
            @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            donationService.rejectDonation(donationId, reason);
            return ResponseEntity.ok(Map.of("message", "Donation rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{donationId}/cancel")
    public ResponseEntity<?> cancelDonation(@PathVariable Long donationId) {
        try {
            donationService.cancelDonation(donationId);
            return ResponseEntity.ok(Map.of("message", "Donation cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{donationId}")
    public ResponseEntity<?> updateDonation(
            @PathVariable Long donationId,
            @Valid @RequestBody DonationDTO donationDTO) {
        try {
            DonationDTO updatedDonation = donationService.updateDonation(donationId, donationDTO);
            return ResponseEntity.ok(updatedDonation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{donationId}")
    public ResponseEntity<?> deleteDonation(@PathVariable Long donationId) {
        try {
            donationService.deleteDonation(donationId);
            return ResponseEntity.ok(Map.of("message", "Donation deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        return 1L; // Placeholder - should be extracted from token
    }
}
