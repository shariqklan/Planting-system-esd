package com.example.Plantation_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Plantation_system.dto.OrganizationDTO;
import com.example.Plantation_system.entities.Organization;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.exceptions.ForbiddenException;
import com.example.Plantation_system.exceptions.ResourceNotFoundException;
import com.example.Plantation_system.mapper.OrganizationMapper;
import com.example.Plantation_system.repositories.OrganizationRepository;
import com.example.Plantation_system.repositories.UserRepository;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    public OrganizationDTO createOrganization(Long ownerUserId, OrganizationDTO organizationDTO) {
        User owner = userRepository.findById(ownerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerUserId));

        Organization organization = new Organization();
        organization.setOwner(owner);
        organization.setOrgName(organizationDTO.getOrgName());
        organization.setContactEmail(organizationDTO.getContactEmail());
        organization.setWebUrl(organizationDTO.getWebUrl());
        organization.setPurpose(organizationDTO.getPurpose());

        Organization savedOrganization = organizationRepository.save(organization);
        return organizationMapper.toDTO(savedOrganization);
    }

    public OrganizationDTO getOrganizationById(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));
        return organizationMapper.toDTO(organization);
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrganizationDTO> getOrganizationsByOwner(Long ownerUserId) {
        return organizationRepository.findByOwnerUserId(ownerUserId).stream()
                .map(organizationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrganizationDTO> searchOrganizationsByName(String name) {
        return organizationRepository.searchByName(name).stream()
                .map(organizationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrganizationDTO updateOrganization(Long organizationId, Long userId, OrganizationDTO organizationDTO) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));

        // Verify ownership
        if (!organization.getOwner().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this organization");
        }

        if (organizationDTO.getOrgName() != null) {
            organization.setOrgName(organizationDTO.getOrgName());
        }

        if (organizationDTO.getContactEmail() != null) {
            organization.setContactEmail(organizationDTO.getContactEmail());
        }

        if (organizationDTO.getWebUrl() != null) {
            organization.setWebUrl(organizationDTO.getWebUrl());
        }

        if (organizationDTO.getPurpose() != null) {
            organization.setPurpose(organizationDTO.getPurpose());
        }

        Organization updatedOrganization = organizationRepository.save(organization);
        return organizationMapper.toDTO(updatedOrganization);
    }

    public void deleteOrganization(Long organizationId, Long userId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));

        // Verify ownership
        if (!organization.getOwner().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this organization");
        }

        organizationRepository.delete(organization);
    }

    public boolean isUserOwner(Long organizationId, Long userId) {
        Organization organization = organizationRepository.findById(organizationId).orElse(null);
        return organization != null && organization.getOwner().getUserId().equals(userId);
    }

    public Integer getOrganizationMeetupCount(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));
        return organization.getMeetups().size();
    }
}
