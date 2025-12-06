package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.DonationDTO;
import com.example.Plantation_system.entities.Donation;
import com.example.Plantation_system.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-06T11:28:07+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class DonationMapperImpl implements DonationMapper {

    @Override
    public DonationDTO toDTO(Donation donation) {
        if ( donation == null ) {
            return null;
        }

        DonationDTO donationDTO = new DonationDTO();

        donationDTO.setDonorUserId( donationDonorUserId( donation ) );
        donationDTO.setDonationId( donation.getDonationId() );
        donationDTO.setAmountDonated( donation.getAmountDonated() );
        donationDTO.setCurrency( donation.getCurrency() );
        donationDTO.setDonationDate( donation.getDonationDate() );
        donationDTO.setTiersId( donation.getTiersId() );
        donationDTO.setTargetType( donation.getTargetType() );
        donationDTO.setTargetId( donation.getTargetId() );
        donationDTO.setStatus( donation.getStatus() );
        donationDTO.setMessage( donation.getMessage() );
        donationDTO.setIdempotencyKey( donation.getIdempotencyKey() );
        donationDTO.setPaymentRef( donation.getPaymentRef() );

        return donationDTO;
    }

    @Override
    public Donation toEntity(DonationDTO donationDTO) {
        if ( donationDTO == null ) {
            return null;
        }

        Donation donation = new Donation();

        donation.setAmountDonated( donationDTO.getAmountDonated() );
        donation.setCurrency( donationDTO.getCurrency() );
        donation.setTiersId( donationDTO.getTiersId() );
        donation.setTargetType( donationDTO.getTargetType() );
        donation.setTargetId( donationDTO.getTargetId() );
        donation.setStatus( donationDTO.getStatus() );
        donation.setMessage( donationDTO.getMessage() );
        donation.setIdempotencyKey( donationDTO.getIdempotencyKey() );
        donation.setPaymentRef( donationDTO.getPaymentRef() );

        return donation;
    }

    private Long donationDonorUserId(Donation donation) {
        if ( donation == null ) {
            return null;
        }
        User donor = donation.getDonor();
        if ( donor == null ) {
            return null;
        }
        Long userId = donor.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
