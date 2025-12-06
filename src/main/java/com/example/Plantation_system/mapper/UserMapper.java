package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.UserDTO;
import com.example.Plantation_system.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "volunteerProfile", ignore = true)
    @Mapping(target = "organizations", ignore = true)
    @Mapping(target = "createdMeetups", ignore = true)
    @Mapping(target = "donations", ignore = true)
    @Mapping(target = "activityLogs", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDTO userDTO);
}
