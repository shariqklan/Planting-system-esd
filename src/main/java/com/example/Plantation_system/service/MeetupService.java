package com.example.Plantation_system.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.MeetupDTO;
import com.example.Plantation_system.entities.Meetup;
import com.example.Plantation_system.entities.Organization;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.ForbiddenException;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.MeetupMapper;
import com.example.Plantation_system.repositories.MeetupRepository;
import com.example.Plantation_system.repositories.OrganizationRepository;
import com.example.Plantation_system.repositories.UserRepository;

@Service
@Transactional
public class MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MeetupMapper meetupMapper;

    public MeetupDTO createMeetup(Long hostUserId, MeetupDTO meetupDTO) {
        User host = userRepository.findById(hostUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + hostUserId));

        Meetup meetup = new Meetup();
        meetup.setHost(host);
        meetup.setMeetupName(meetupDTO.getMeetupName());
        meetup.setMeetupDate(convertToLocalDate(meetupDTO.getMeetupDate()));
        meetup.setStartTime(convertToLocalTime(meetupDTO.getStartTime()));
        meetup.setDurationMinutes(parseDuration(meetupDTO.getDuration()));
        meetup.setDescription(meetupDTO.getDescription());
        meetup.setCapacity(meetupDTO.getCapacity() != null ? meetupDTO.getCapacity() : Integer.valueOf(50));
        meetup.setStatus("SCHEDULED");

        // Set organization if provided
        if (meetupDTO.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(meetupDTO.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
            meetup.setOrganization(organization);
        }

        Meetup savedMeetup = meetupRepository.save(meetup);
        return meetupMapper.toDTO(savedMeetup);
    }

    public MeetupDTO getMeetupById(Long meetupId) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));
        return meetupMapper.toDTO(meetup);
    }

    public List<MeetupDTO> getAllMeetups() {
        return meetupRepository.findAll().stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupDTO> getMeetupsByOrganization(Long organizationId) {
        return meetupRepository.findByOrganizationId(organizationId).stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupDTO> getMeetupsByHost(Long hostUserId) {
        return meetupRepository.findByHostUserId(hostUserId).stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupDTO> getMeetupsByStatus(String status) {
        return meetupRepository.findByStatus(status).stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupDTO> getUpcomingMeetups(Long organizationId) {
        return meetupRepository.findUpcomingMeetupsByOrganization(organizationId).stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupDTO> getMeetupsByDateRange(LocalDate startDate, LocalDate endDate) {
        return meetupRepository.findMeetupsByDateRange(startDate, endDate).stream()
                .map(meetupMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MeetupDTO updateMeetup(Long meetupId, Long userId, MeetupDTO meetupDTO) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));

        // Verify ownership
        if (!meetup.getHost().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this meetup");
        }

        if (meetupDTO.getMeetupName() != null) {
            meetup.setMeetupName(meetupDTO.getMeetupName());
        }

        if (meetupDTO.getMeetupDate() != null) {
            meetup.setMeetupDate(convertToLocalDate(meetupDTO.getMeetupDate()));
        }

        if (meetupDTO.getStartTime() != null) {
            meetup.setStartTime(convertToLocalTime(meetupDTO.getStartTime()));
        }

        if (meetupDTO.getDuration() != null) {
            meetup.setDurationMinutes(parseDuration(meetupDTO.getDuration()));
        }

        if (meetupDTO.getDescription() != null) {
            meetup.setDescription(meetupDTO.getDescription());
        }

        if (meetupDTO.getCapacity() != null) {
            meetup.setCapacity(meetupDTO.getCapacity());
        }

        if (meetupDTO.getStatus() != null) {
            meetup.setStatus(meetupDTO.getStatus());
        }

        Meetup updatedMeetup = meetupRepository.save(meetup);
        return meetupMapper.toDTO(updatedMeetup);
    }

    public void deleteMeetup(Long meetupId, Long userId) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));

        // Verify ownership
        if (!meetup.getHost().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this meetup");
        }

        meetupRepository.delete(meetup);
    }

    public void updateMeetupStatus(Long meetupId, String newStatus) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));
        meetup.setStatus(newStatus);
        meetupRepository.save(meetup);
    }

    public Integer getParticipantCount(Long meetupId) {
        return meetupRepository.countParticipantsByMeetupId(meetupId);
    }

    public boolean canJoinMeetup(Long meetupId) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));

        Integer currentParticipants = getParticipantCount(meetupId);
        return currentParticipants < meetup.getCapacity() && "SCHEDULED".equals(meetup.getStatus());
    }

    private Integer parseDuration(String duration) {
        // Parse duration string like "PT2H" or "120" minutes
        if (duration == null) return 120;
        
        try {
            // If it's a number, treat as minutes
            if (duration.matches("\\d+")) {
                return Integer.valueOf(duration);
            }
            // If it's ISO-8601 duration format
            if (duration.startsWith("PT")) {
                // Simple parsing: PT2H -> 120 minutes, PT30M -> 30 minutes
                if (duration.contains("H")) {
                    int hours = Integer.parseInt(duration.substring(2, duration.indexOf("H")));
                    return hours * 60;
                } else if (duration.contains("M")) {
                    int minutes = Integer.parseInt(duration.substring(2, duration.indexOf("M")));
                    return minutes;
                }
            }
        } catch (NumberFormatException e) {
            return 120; // Default 2 hours
        }
        return 120;
    }

    private LocalDate convertToLocalDate(java.sql.Date sqlDate) {
        if (sqlDate == null) return null;
        return sqlDate.toLocalDate();
    }

    private LocalTime convertToLocalTime(java.sql.Time sqlTime) {
        if (sqlTime == null) return null;
        return sqlTime.toLocalTime();
    }
}
