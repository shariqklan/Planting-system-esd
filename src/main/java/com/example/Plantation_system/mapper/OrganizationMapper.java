package com.example.Plantation_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Plantation_system.dto.OrganizationDTO;
import com.example.Plantation_system.entities.Organization;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "ownerUserId", source = "owner.userId")
    OrganizationDTO toDTO(Organization organization);

    @Mapping(target = "organizationId", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "meetups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);
}
