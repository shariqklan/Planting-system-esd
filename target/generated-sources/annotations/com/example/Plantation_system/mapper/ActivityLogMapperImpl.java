package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.ActivityLogDTO;
import com.example.Plantation_system.entities.ActivityLog;
import com.example.Plantation_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T13:55:51+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ActivityLogMapperImpl implements ActivityLogMapper {

    @Override
    public ActivityLogDTO toDTO(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }

        ActivityLogDTO activityLogDTO = new ActivityLogDTO();

        activityLogDTO.setUserId( activityLogUserUserId( activityLog ) );
        activityLogDTO.setLogId( activityLog.getLogId() );
        activityLogDTO.setActionType( activityLog.getActionType() );
        activityLogDTO.setEntityType( activityLog.getEntityType() );
        activityLogDTO.setEntityId( activityLog.getEntityId() );
        activityLogDTO.setDetail( activityLog.getDetail() );
        activityLogDTO.setIpAddress( activityLog.getIpAddress() );
        activityLogDTO.setUserAgent( activityLog.getUserAgent() );
        activityLogDTO.setCreatedAt( activityLog.getCreatedAt() );

        return activityLogDTO;
    }

    @Override
    public ActivityLog toEntity(ActivityLogDTO activityLogDTO) {
        if ( activityLogDTO == null ) {
            return null;
        }

        ActivityLog activityLog = new ActivityLog();

        activityLog.setActionType( activityLogDTO.getActionType() );
        activityLog.setEntityType( activityLogDTO.getEntityType() );
        activityLog.setEntityId( activityLogDTO.getEntityId() );
        activityLog.setDetail( activityLogDTO.getDetail() );
        activityLog.setIpAddress( activityLogDTO.getIpAddress() );
        activityLog.setUserAgent( activityLogDTO.getUserAgent() );

        return activityLog;
    }

    private Long activityLogUserUserId(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }
        User user = activityLog.getUser();
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
