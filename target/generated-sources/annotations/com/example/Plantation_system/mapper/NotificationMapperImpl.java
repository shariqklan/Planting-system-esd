package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.NotificationDTO;
import com.example.Plantation_system.entities.Notification;
import com.example.Plantation_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-06T11:28:07+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDTO toDTO(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId( notificationUserUserId( notification ) );
        notificationDTO.setNotificationId( notification.getNotificationId() );
        notificationDTO.setType( notification.getType() );
        notificationDTO.setChannel( notification.getChannel() );
        notificationDTO.setSubject( notification.getSubject() );
        notificationDTO.setBody( notification.getBody() );
        notificationDTO.setPayload( notification.getPayload() );
        notificationDTO.setStatus( notification.getStatus() );
        notificationDTO.setSentAt( notification.getSentAt() );
        notificationDTO.setDeliveredAt( notification.getDeliveredAt() );
        notificationDTO.setReadAt( notification.getReadAt() );
        notificationDTO.setCreatedAt( notification.getCreatedAt() );

        return notificationDTO;
    }

    @Override
    public Notification toEntity(NotificationDTO notificationDTO) {
        if ( notificationDTO == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setType( notificationDTO.getType() );
        notification.setChannel( notificationDTO.getChannel() );
        notification.setSubject( notificationDTO.getSubject() );
        notification.setBody( notificationDTO.getBody() );
        notification.setPayload( notificationDTO.getPayload() );
        notification.setStatus( notificationDTO.getStatus() );
        notification.setDeliveredAt( notificationDTO.getDeliveredAt() );
        notification.setReadAt( notificationDTO.getReadAt() );

        return notification;
    }

    private Long notificationUserUserId(Notification notification) {
        if ( notification == null ) {
            return null;
        }
        User user = notification.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
