package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.OrganizationDTO;
import com.example.Plantation_system.entities.Organization;
import com.example.Plantation_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T13:55:51+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public OrganizationDTO toDTO(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationDTO organizationDTO = new OrganizationDTO();

        organizationDTO.setOwnerUserId( organizationOwnerUserId( organization ) );
        organizationDTO.setOrganizationId( organization.getOrganizationId() );
        organizationDTO.setOrgName( organization.getOrgName() );
        organizationDTO.setContactEmail( organization.getContactEmail() );
        organizationDTO.setWebUrl( organization.getWebUrl() );
        organizationDTO.setPurpose( organization.getPurpose() );
        organizationDTO.setCreatedAt( organization.getCreatedAt() );

        return organizationDTO;
    }

    @Override
    public Organization toEntity(OrganizationDTO organizationDTO) {
        if ( organizationDTO == null ) {
            return null;
        }

        Organization organization = new Organization();

        organization.setOrgName( organizationDTO.getOrgName() );
        organization.setContactEmail( organizationDTO.getContactEmail() );
        organization.setWebUrl( organizationDTO.getWebUrl() );
        organization.setPurpose( organizationDTO.getPurpose() );

        return organization;
    }

    private Long organizationOwnerUserId(Organization organization) {
        if ( organization == null ) {
            return null;
        }
        User owner = organization.getOwner();
        if ( owner == null ) {
            return null;
        }
        Long userId = owner.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
