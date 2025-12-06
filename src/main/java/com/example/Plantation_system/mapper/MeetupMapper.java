package com.example.Plantation_system.mapper;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.MeetupDTO;
import com.example.Plantation_system.entities.Meetup;

@Mapper(componentModel = "spring")
public interface MeetupMapper {

    @Mapping(target = "hostUserId", source = "host.userId")
    @Mapping(target = "organizationId", source = "organization.organizationId")
    @Mapping(target = "meetupDate", source = "meetupDate", qualifiedByName = "localDateToSqlDate")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "localTimeToSqlTime")
    @Mapping(target = "duration", source = "durationMinutes", qualifiedByName = "integerToString")
    MeetupDTO toDTO(Meetup meetup);

    @Mapping(target = "meetupId", ignore = true)
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "donations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "meetupDate", source = "meetupDate", qualifiedByName = "sqlDateToLocalDate")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "sqlTimeToLocalTime")
    @Mapping(target = "durationMinutes", source = "duration", qualifiedByName = "stringToInteger")
    Meetup toEntity(MeetupDTO meetupDTO);

    @org.mapstruct.Named("localDateToSqlDate")
    default Date localDateToSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    @org.mapstruct.Named("sqlDateToLocalDate")
    default LocalDate sqlDateToLocalDate(Date sqlDate) {
        return sqlDate == null ? null : sqlDate.toLocalDate();
    }

    @org.mapstruct.Named("localTimeToSqlTime")
    default Time localTimeToSqlTime(LocalTime localTime) {
        return localTime == null ? null : Time.valueOf(localTime);
    }

    @org.mapstruct.Named("sqlTimeToLocalTime")
    default LocalTime sqlTimeToLocalTime(Time sqlTime) {
        return sqlTime == null ? null : sqlTime.toLocalTime();
    }

    @org.mapstruct.Named("integerToString")
    default String integerToString(Integer value) {
        return value == null ? null : value.toString();
    }

    @org.mapstruct.Named("stringToInteger")
    default Integer stringToInteger(String value) {
        if (value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 120; // Default 2 hours
        }
    }
}
