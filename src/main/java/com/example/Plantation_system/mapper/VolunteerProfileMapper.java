package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.VolunteerProfileDTO;
import com.example.Plantation_system.entities.VolunteerProfile;

@Mapper(componentModel = "spring")
public interface VolunteerProfileMapper {

    @Mapping(target = "userId", source = "user.userId")
    VolunteerProfileDTO toDTO(VolunteerProfile volunteerProfile);

    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VolunteerProfile toEntity(VolunteerProfileDTO volunteerProfileDTO);
}
