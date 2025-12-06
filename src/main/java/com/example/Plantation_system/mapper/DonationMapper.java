package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.DonationDTO;
import com.example.Plantation_system.entities.Donation;

@Mapper(componentModel = "spring")
public interface DonationMapper {

    @Mapping(target = "donorUserId", source = "donor.userId")
    @Mapping(target = "targetMeetupId", ignore = true)
    @Mapping(target = "targetOrganizationId", ignore = true)
    DonationDTO toDTO(Donation donation);

    @Mapping(target = "donationId", ignore = true)
    @Mapping(target = "donor", ignore = true)
    @Mapping(target = "targetMeetup", ignore = true)
    @Mapping(target = "targetOrganization", ignore = true)
    @Mapping(target = "donationDate", ignore = true)
    @Mapping(target = "payments", ignore = true)
    Donation toEntity(DonationDTO donationDTO);
}
