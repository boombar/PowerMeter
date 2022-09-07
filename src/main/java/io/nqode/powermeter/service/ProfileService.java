package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.ProfileDto;
import io.nqode.powermeter.dto.ProfileFractionsDto;
import io.nqode.powermeter.dto.UploadFractionDto;
import io.nqode.powermeter.model.Profile;

import java.util.List;

public interface ProfileService {

    List<Profile> getAll();

    Profile getById(Long id);

    Profile createProfile(ProfileDto profileDto);

    Profile updateProfile(Long profileId, ProfileDto profileDto);

    void deleteProfile(Long profileId);

    Profile getProfileByName(String name);

    ProfileFractionsDto saveProfileWithFractions(String profileName, List<UploadFractionDto> uploadFractionDtos);
}
