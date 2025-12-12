package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.MeetupDTO;
import com.example.Plantation_system.entities.Meetup;
import com.example.Plantation_system.entities.Organization;
import com.example.Plantation_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T13:55:50+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class MeetupMapperImpl implements MeetupMapper {

    @Override
    public MeetupDTO toDTO(Meetup meetup) {
        if ( meetup == null ) {
            return null;
        }

        MeetupDTO meetupDTO = new MeetupDTO();

        meetupDTO.setHostUserId( meetupHostUserId( meetup ) );
        meetupDTO.setOrganizationId( meetupOrganizationOrganizationId( meetup ) );
        meetupDTO.setMeetupDate( localDateToSqlDate( meetup.getMeetupDate() ) );
        meetupDTO.setStartTime( localTimeToSqlTime( meetup.getStartTime() ) );
        meetupDTO.setDuration( integerToString( meetup.getDurationMinutes() ) );
        meetupDTO.setMeetupId( meetup.getMeetupId() );
        meetupDTO.setMeetupName( meetup.getMeetupName() );
        meetupDTO.setDescription( meetup.getDescription() );
        meetupDTO.setCapacity( meetup.getCapacity() );
        meetupDTO.setStatus( meetup.getStatus() );
        meetupDTO.setCreatedAt( meetup.getCreatedAt() );
        meetupDTO.setUpdatedAt( meetup.getUpdatedAt() );

        return meetupDTO;
    }

    @Override
    public Meetup toEntity(MeetupDTO meetupDTO) {
        if ( meetupDTO == null ) {
            return null;
        }

        Meetup meetup = new Meetup();

        meetup.setMeetupDate( sqlDateToLocalDate( meetupDTO.getMeetupDate() ) );
        meetup.setStartTime( sqlTimeToLocalTime( meetupDTO.getStartTime() ) );
        meetup.setDurationMinutes( stringToInteger( meetupDTO.getDuration() ) );
        meetup.setMeetupName( meetupDTO.getMeetupName() );
        meetup.setDescription( meetupDTO.getDescription() );
        meetup.setCapacity( meetupDTO.getCapacity() );
        meetup.setStatus( meetupDTO.getStatus() );

        return meetup;
    }

    private Long meetupHostUserId(Meetup meetup) {
        if ( meetup == null ) {
            return null;
        }
        User host = meetup.getHost();
        if ( host == null ) {
            return null;
        }
        Long userId = host.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long meetupOrganizationOrganizationId(Meetup meetup) {
        if ( meetup == null ) {
            return null;
        }
        Organization organization = meetup.getOrganization();
        if ( organization == null ) {
            return null;
        }
        Long organizationId = organization.getOrganizationId();
        if ( organizationId == null ) {
            return null;
        }
        return organizationId;
    }
}
