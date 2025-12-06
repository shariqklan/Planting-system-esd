package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.ActivityLogDTO;
import com.example.Plantation_system.entities.ActivityLog;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

    @Mapping(target = "userId", source = "user.userId")
    ActivityLogDTO toDTO(ActivityLog activityLog);

    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ActivityLog toEntity(ActivityLogDTO activityLogDTO);
}
