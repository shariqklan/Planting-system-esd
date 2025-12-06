package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.NotificationDTO;
import com.example.Plantation_system.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.userId")
    NotificationDTO toDTO(Notification notification);

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "sentAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toEntity(NotificationDTO notificationDTO);
}
