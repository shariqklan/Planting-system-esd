package com.example.Plantation_system.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.VolunteerProfileDTO;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.entities.VolunteerProfile;
import com.example.Plantation_system.exceptions.BadRequestException;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.VolunteerProfileMapper;
import com.example.Plantation_system.repositories.UserRepository;
import com.example.Plantation_system.repositories.VolunteerProfileRepository;

@Service
@Transactional
public class VolunteerProfileService {

    @Autowired
    private VolunteerProfileRepository volunteerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VolunteerProfileMapper volunteerProfileMapper;

    public VolunteerProfileDTO createVolunteerProfile(Long userId, VolunteerProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (volunteerProfileRepository.findByUserUserId(userId).isPresent()) {
            throw new BadRequestException("Volunteer profile already exists for this user");
        }

        VolunteerProfile profile = new VolunteerProfile();
        profile.setUser(user);
        profile.setBio(profileDTO.getBio());
        if (profileDTO.getSkills() != null) {
            profile.setSkills(Arrays.asList(profileDTO.getSkills()));
        }
        profile.setTotalHours(BigDecimal.ZERO);
        profile.setRating(0.0);
        profile.setJoinedMeetupsCount(0);

        VolunteerProfile savedProfile = volunteerProfileRepository.save(profile);
        return volunteerProfileMapper.toDTO(savedProfile);
    }

    public VolunteerProfileDTO getVolunteerProfileByUserId(Long userId) {
        VolunteerProfile profile = volunteerProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found for user id: " + userId));
        return volunteerProfileMapper.toDTO(profile);
    }

    public VolunteerProfileDTO getVolunteerProfileById(Long profileId) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));
        return volunteerProfileMapper.toDTO(profile);
    }

    public List<VolunteerProfileDTO> getAllVolunteerProfiles() {
        return volunteerProfileRepository.findAll().stream()
                .map(volunteerProfileMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VolunteerProfileDTO updateVolunteerProfile(Long profileId, VolunteerProfileDTO profileDTO) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        if (profileDTO.getBio() != null) {
            profile.setBio(profileDTO.getBio());
        }

        if (profileDTO.getSkills() != null) {
            profile.setSkills(Arrays.asList(profileDTO.getSkills()));
        }

        VolunteerProfile updatedProfile = volunteerProfileRepository.save(profile);
        return volunteerProfileMapper.toDTO(updatedProfile);
    }

    public void addSkill(Long profileId, String skill) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        if (!profile.getSkills().contains(skill)) {
            profile.getSkills().add(skill);
            volunteerProfileRepository.save(profile);
        }
    }

    public void removeSkill(Long profileId, String skill) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        profile.getSkills().remove(skill);
        volunteerProfileRepository.save(profile);
    }

    public void updateTotalHours(Long profileId, BigDecimal hours) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        BigDecimal newTotal = profile.getTotalHours().add(hours);
        profile.setTotalHours(newTotal);
        volunteerProfileRepository.save(profile);
    }

    public void updateRating(Long profileId, Double rating) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        if (rating < 0 || rating > 5) {
            throw new BadRequestException("Rating must be between 0 and 5");
        }

        profile.setRating(rating);
        volunteerProfileRepository.save(profile);
    }

    public void incrementMeetupCount(Long profileId) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));

        profile.setJoinedMeetupsCount(profile.getJoinedMeetupsCount() + 1);
        volunteerProfileRepository.save(profile);
    }

    public List<VolunteerProfileDTO> getTopVolunteersByRating(Double minRating) {
        return volunteerProfileRepository.findTopVolunteersByRating(minRating).stream()
                .map(volunteerProfileMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VolunteerProfileDTO> getMostActiveVolunteers() {
        return volunteerProfileRepository.findMostActiveVolunteers().stream()
                .map(volunteerProfileMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteVolunteerProfile(Long profileId) {
        VolunteerProfile profile = volunteerProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + profileId));
        volunteerProfileRepository.delete(profile);
    }
}
