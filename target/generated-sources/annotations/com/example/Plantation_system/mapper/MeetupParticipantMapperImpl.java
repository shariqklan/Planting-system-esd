package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.MeetupParticipantDTO;
import com.example.Plantation_system.entities.Meetup;
import com.example.Plantation_system.entities.MeetupParticipant;
import com.example.Plantation_system.entities.VolunteerProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T13:55:51+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class MeetupParticipantMapperImpl implements MeetupParticipantMapper {

    @Override
    public MeetupParticipantDTO toDTO(MeetupParticipant meetupParticipant) {
        if ( meetupParticipant == null ) {
            return null;
        }

        MeetupParticipantDTO meetupParticipantDTO = new MeetupParticipantDTO();

        meetupParticipantDTO.setMeetupId( meetupParticipantMeetupMeetupId( meetupParticipant ) );
        meetupParticipantDTO.setParticipantId( meetupParticipantParticipantProfileId( meetupParticipant ) );
        meetupParticipantDTO.setStatus( meetupParticipant.getStatus() );
        meetupParticipantDTO.setJoinedAt( meetupParticipant.getJoinedAt() );
        meetupParticipantDTO.setAttendedAt( meetupParticipant.getAttendedAt() );
        meetupParticipantDTO.setHoursContributed( meetupParticipant.getHoursContributed() );
        meetupParticipantDTO.setNote( meetupParticipant.getNote() );

        return meetupParticipantDTO;
    }

    @Override
    public MeetupParticipant toEntity(MeetupParticipantDTO meetupParticipantDTO) {
        if ( meetupParticipantDTO == null ) {
            return null;
        }

        MeetupParticipant meetupParticipant = new MeetupParticipant();

        meetupParticipant.setStatus( meetupParticipantDTO.getStatus() );
        meetupParticipant.setAttendedAt( meetupParticipantDTO.getAttendedAt() );
        meetupParticipant.setHoursContributed( meetupParticipantDTO.getHoursContributed() );
        meetupParticipant.setNote( meetupParticipantDTO.getNote() );

        return meetupParticipant;
    }

    private Long meetupParticipantMeetupMeetupId(MeetupParticipant meetupParticipant) {
        if ( meetupParticipant == null ) {
            return null;
        }
        Meetup meetup = meetupParticipant.getMeetup();
        if ( meetup == null ) {
            return null;
        }
        Long meetupId = meetup.getMeetupId();
        if ( meetupId == null ) {
            return null;
        }
        return meetupId;
    }

    private Long meetupParticipantParticipantProfileId(MeetupParticipant meetupParticipant) {
        if ( meetupParticipant == null ) {
            return null;
        }
        VolunteerProfile participant = meetupParticipant.getParticipant();
        if ( participant == null ) {
            return null;
        }
        Long profileId = participant.getProfileId();
        if ( profileId == null ) {
            return null;
        }
        return profileId;
    }
}
