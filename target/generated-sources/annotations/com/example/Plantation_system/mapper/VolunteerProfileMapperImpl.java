package com.example.Plantation_system.mapper;

import com.example.Plantation_system.dto.VolunteerProfileDTO;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.entities.VolunteerProfile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T13:55:51+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class VolunteerProfileMapperImpl implements VolunteerProfileMapper {

    @Override
    public VolunteerProfileDTO toDTO(VolunteerProfile volunteerProfile) {
        if ( volunteerProfile == null ) {
            return null;
        }

        VolunteerProfileDTO volunteerProfileDTO = new VolunteerProfileDTO();

        volunteerProfileDTO.setUserId( volunteerProfileUserUserId( volunteerProfile ) );
        volunteerProfileDTO.setProfileId( volunteerProfile.getProfileId() );
        volunteerProfileDTO.setBio( volunteerProfile.getBio() );
        volunteerProfileDTO.setSkills( stringListToStringArray( volunteerProfile.getSkills() ) );
        volunteerProfileDTO.setTotalHours( volunteerProfile.getTotalHours() );
        if ( volunteerProfile.getRating() != null ) {
            volunteerProfileDTO.setRating( BigDecimal.valueOf( volunteerProfile.getRating() ) );
        }
        volunteerProfileDTO.setJoinedMeetupsCount( volunteerProfile.getJoinedMeetupsCount() );
        volunteerProfileDTO.setCreatedAt( volunteerProfile.getCreatedAt() );
        volunteerProfileDTO.setUpdatedAt( volunteerProfile.getUpdatedAt() );

        return volunteerProfileDTO;
    }

    @Override
    public VolunteerProfile toEntity(VolunteerProfileDTO volunteerProfileDTO) {
        if ( volunteerProfileDTO == null ) {
            return null;
        }

        VolunteerProfile volunteerProfile = new VolunteerProfile();

        volunteerProfile.setBio( volunteerProfileDTO.getBio() );
        volunteerProfile.setSkills( stringArrayToStringList( volunteerProfileDTO.getSkills() ) );
        volunteerProfile.setTotalHours( volunteerProfileDTO.getTotalHours() );
        if ( volunteerProfileDTO.getRating() != null ) {
            volunteerProfile.setRating( volunteerProfileDTO.getRating().doubleValue() );
        }
        volunteerProfile.setJoinedMeetupsCount( volunteerProfileDTO.getJoinedMeetupsCount() );

        return volunteerProfile;
    }

    private Long volunteerProfileUserUserId(VolunteerProfile volunteerProfile) {
        if ( volunteerProfile == null ) {
            return null;
        }
        User user = volunteerProfile.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    protected String[] stringListToStringArray(List<String> list) {
        if ( list == null ) {
            return null;
        }

        String[] stringTmp = new String[list.size()];
        int i = 0;
        for ( String string : list ) {
            stringTmp[i] = string;
            i++;
        }

        return stringTmp;
    }

    protected List<String> stringArrayToStringList(String[] stringArray) {
        if ( stringArray == null ) {
            return null;
        }

        List<String> list = new ArrayList<String>( stringArray.length );
        for ( String string : stringArray ) {
            list.add( string );
        }

        return list;
    }
}
