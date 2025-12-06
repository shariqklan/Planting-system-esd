package com.example.Plantation_system.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.MeetupParticipantDTO;
import com.example.Plantation_system.entities.Meetup;
import com.example.Plantation_system.entities.MeetupParticipant;
import com.example.Plantation_system.entities.MeetupParticipantId;
import com.example.Plantation_system.entities.VolunteerProfile;
import com.example.Plantation_system.exceptions.BadRequestException;
import com.example.Plantation_system.exceptions.ConflictException;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.MeetupParticipantMapper;
import com.example.Plantation_system.repositories.MeetupParticipantRepository;
import com.example.Plantation_system.repositories.MeetupRepository;
import com.example.Plantation_system.repositories.VolunteerProfileRepository;

@Service
@Transactional
public class MeetupParticipantService {

    @Autowired
    private MeetupParticipantRepository meetupParticipantRepository;

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private VolunteerProfileRepository volunteerProfileRepository;

    @Autowired
    private MeetupParticipantMapper meetupParticipantMapper;

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private VolunteerProfileService volunteerProfileService;

    public MeetupParticipantDTO joinMeetup(Long meetupId, Long participantId) {
        Meetup meetup = meetupRepository.findById(meetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Meetup not found with id: " + meetupId));

        VolunteerProfile participant = volunteerProfileRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found with id: " + participantId));

        // Check if already joined
        if (meetupParticipantRepository.isUserParticipantInMeetup(meetupId, participantId)) {
            throw new ConflictException("User is already a participant in this meetup");
        }

        // Check capacity
        if (!meetupService.canJoinMeetup(meetupId)) {
            throw new BadRequestException("Cannot join meetup - at capacity or not scheduled");
        }

        MeetupParticipantId id = new MeetupParticipantId(meetupId, participantId);
        MeetupParticipant meetupParticipant = new MeetupParticipant();
        meetupParticipant.setId(id);
        meetupParticipant.setMeetup(meetup);
        meetupParticipant.setParticipant(participant);
        meetupParticipant.setStatus("JOINED");

        MeetupParticipant savedParticipant = meetupParticipantRepository.save(meetupParticipant);
        
        // Increment volunteer meetup count
        volunteerProfileService.incrementMeetupCount(participantId);

        return meetupParticipantMapper.toDTO(savedParticipant);
    }

    public void leaveMeetup(Long meetupId, Long participantId) {
        MeetupParticipantId id = new MeetupParticipantId(meetupId, participantId);
        MeetupParticipant participant = meetupParticipantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found in this meetup"));

        meetupParticipantRepository.delete(participant);
    }

    public List<MeetupParticipantDTO> getParticipantsByMeetup(Long meetupId) {
        return meetupParticipantRepository.findByMeetupId(meetupId).stream()
                .map(meetupParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeetupParticipantDTO> getMeetupsByParticipant(Long participantId) {
        return meetupParticipantRepository.findByParticipantId(participantId).stream()
                .map(meetupParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void markAttendance(Long meetupId, Long participantId) {
        MeetupParticipantId id = new MeetupParticipantId(meetupId, participantId);
        MeetupParticipant participant = meetupParticipantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found in this meetup"));

        participant.setStatus("ATTENDED");
        participant.setAttendedAt(Instant.now());
        meetupParticipantRepository.save(participant);
    }

    public void updateHoursContributed(Long meetupId, Long participantId, BigDecimal hours) {
        MeetupParticipantId id = new MeetupParticipantId(meetupId, participantId);
        MeetupParticipant participant = meetupParticipantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found in this meetup"));

        if (hours.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Hours contributed cannot be negative");
        }

        participant.setHoursContributed(hours);
        meetupParticipantRepository.save(participant);

        // Update volunteer profile total hours
        volunteerProfileService.updateTotalHours(participantId, hours);
    }

    public void addNote(Long meetupId, Long participantId, String note) {
        MeetupParticipantId id = new MeetupParticipantId(meetupId, participantId);
        MeetupParticipant participant = meetupParticipantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found in this meetup"));

        participant.setNote(note);
        meetupParticipantRepository.save(participant);
    }

    public List<MeetupParticipantDTO> getAttendeesByMeetup(Long meetupId) {
        return meetupParticipantRepository.findByMeetupAndStatus(meetupId, "ATTENDED").stream()
                .map(meetupParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Integer getAttendanceCount(Long meetupId) {
        return meetupParticipantRepository.findByMeetupAndStatus(meetupId, "ATTENDED").size();
    }

    public boolean isUserJoinedMeetup(Long meetupId, Long participantId) {
        return meetupParticipantRepository.isUserParticipantInMeetup(meetupId, participantId);
    }
}
