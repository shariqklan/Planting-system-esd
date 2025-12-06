package com.example.Plantation_system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.DonationDTO;
import com.example.Plantation_system.entities.Donation;
import com.example.Plantation_system.entities.DonationTargetType;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.BadRequestException;
import com.example.Plantation_system.exceptions.ConflictException;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.DonationMapper;
import com.example.Plantation_system.repositories.DonationRepository;
import com.example.Plantation_system.repositories.UserRepository;

@Service
@Transactional
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationMapper donationMapper;

    public DonationDTO createDonation(Long donorUserId, DonationDTO donationDTO) {
        User donor = userRepository.findById(donorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + donorUserId));

        // Check for idempotency
        if (donationDTO.getIdempotencyKey() != null) {
            if (donationRepository.findByIdempotencyKey(donationDTO.getIdempotencyKey()).isPresent()) {
                throw new ConflictException("Donation with this idempotency key already exists");
            }
        }

        // Validate donation amount
        if (donationDTO.getAmountDonated() == null || donationDTO.getAmountDonated().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Donation amount must be greater than 0");
        }

        // Validate target
        if (donationDTO.getTargetType() == null || donationDTO.getTargetId() == null) {
            throw new BadRequestException("Target type and target ID are required");
        }

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setAmountDonated(donationDTO.getAmountDonated());
        donation.setCurrency(donationDTO.getCurrency() != null ? donationDTO.getCurrency() : "PKR");
        donation.setTargetType(donationDTO.getTargetType());
        donation.setTargetId(donationDTO.getTargetId());
        donation.setStatus(donationDTO.getStatus() != null ? donationDTO.getStatus() : "PENDING");
        donation.setMessage(donationDTO.getMessage());
        donation.setIdempotencyKey(donationDTO.getIdempotencyKey());

        Donation savedDonation = donationRepository.save(donation);
        return donationMapper.toDTO(savedDonation);
    }

    public DonationDTO getDonationById(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));
        return donationMapper.toDTO(donation);
    }

    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(donationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationDTO> getDonationsByDonor(Long donorUserId) {
        return donationRepository.findByDonorUserId(donorUserId).stream()
                .map(donationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationDTO> getDonationsByTarget(Long targetId, DonationTargetType targetType) {
        return donationRepository.findByTargetId(targetId).stream()
                .filter(d -> d.getTargetType() == targetType)
                .map(donationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationDTO> getDonationsByStatus(String status) {
        return donationRepository.findByStatus(status).stream()
                .map(donationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDonationsByTarget(Long targetId, DonationTargetType targetType) {
        BigDecimal total = donationRepository.getTotalDonationsByTarget(targetId, targetType);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Integer getDonationCountByTarget(Long targetId, DonationTargetType targetType) {
        return donationRepository.countDonationsByTarget(targetId, targetType);
    }

    public void approveDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));

        if (!"PENDING".equals(donation.getStatus())) {
            throw new BadRequestException("Only PENDING donations can be approved");
        }

        donation.setStatus("COMPLETED");
        donationRepository.save(donation);
    }

    public void rejectDonation(Long donationId, String reason) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));

        if (!"PENDING".equals(donation.getStatus())) {
            throw new BadRequestException("Only PENDING donations can be rejected");
        }

        donation.setStatus("REJECTED");
        donation.setMessage(reason);
        donationRepository.save(donation);
    }

    public void cancelDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));

        if (!"PENDING".equals(donation.getStatus())) {
            throw new BadRequestException("Only PENDING donations can be cancelled");
        }

        donation.setStatus("CANCELLED");
        donationRepository.save(donation);
    }

    public DonationDTO updateDonation(Long donationId, DonationDTO donationDTO) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));

        if (!"PENDING".equals(donation.getStatus())) {
            throw new BadRequestException("Only PENDING donations can be updated");
        }

        if (donationDTO.getMessage() != null) {
            donation.setMessage(donationDTO.getMessage());
        }

        Donation updatedDonation = donationRepository.save(donation);
        return donationMapper.toDTO(updatedDonation);
    }

    public void deleteDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + donationId));

        if (!"PENDING".equals(donation.getStatus())) {
            throw new BadRequestException("Only PENDING donations can be deleted");
        }

        donationRepository.delete(donation);
    }
}
