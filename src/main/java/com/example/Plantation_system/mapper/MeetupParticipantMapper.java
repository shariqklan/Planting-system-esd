package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.MeetupParticipantDTO;
import com.example.Plantation_system.entities.MeetupParticipant;

@Mapper(componentModel = "spring")
public interface MeetupParticipantMapper {

    @Mapping(target = "meetupId", source = "meetup.meetupId")
    @Mapping(target = "participantId", source = "participant.profileId")
    MeetupParticipantDTO toDTO(MeetupParticipant meetupParticipant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meetup", ignore = true)
    @Mapping(target = "participant", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    MeetupParticipant toEntity(MeetupParticipantDTO meetupParticipantDTO);
}
