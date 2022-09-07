package io.nqode.powermeter.converter;

import io.nqode.powermeter.dto.ProfileDto;
import io.nqode.powermeter.model.Profile;

public class ProfileConverter {

    private ProfileConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static ProfileDto convertToDto(Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .build();
    }

    public static Profile convertToEntity(ProfileDto profileDto) {
        return Profile.builder()
                .name(profileDto.getName())
                .build();
    }

}
