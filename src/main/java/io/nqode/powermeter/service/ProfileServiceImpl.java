package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.dto.ProfileDto;
import io.nqode.powermeter.dto.ProfileFractionsDto;
import io.nqode.powermeter.dto.UploadFractionDto;
import io.nqode.powermeter.exception.EntityExistException;
import io.nqode.powermeter.exception.EntityNotFoundException;
import io.nqode.powermeter.model.Fraction;
import io.nqode.powermeter.model.Profile;
import io.nqode.powermeter.repository.FractionRepository;
import io.nqode.powermeter.repository.ProfileRepository;
import io.nqode.powermeter.util.FractionUtil;
import io.nqode.powermeter.validation.FractionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static io.nqode.powermeter.converter.FractionConverter.convertUploadDtoToDto;
import static io.nqode.powermeter.converter.ProfileConverter.convertToEntity;
import static io.nqode.powermeter.util.FractionUtil.fillFractionValues;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final FractionRepository fractionRepository;

    private final FractionValidator fractionValidator;

    @Override
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    @Override
    public Profile getById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No profile with id: %d".formatted(id)));
    }

    @Override
    @Transactional
    public Profile createProfile(ProfileDto profileDto) {
        profileRepository.findByName(profileDto.getName()).ifPresent(profile -> {
            throw new EntityExistException("Profile with name %s already exist".formatted(profileDto.getName()));
        });
        return profileRepository.save(convertToEntity(profileDto));
    }

    @Override
    @Transactional
    public Profile updateProfile(Long id, ProfileDto profileDto) {
        Profile profile = getById(id);
        if (!profile.getName().equals(profileDto.getName())) {
            profileRepository.findByName(profileDto.getName()).ifPresent(foundProfile -> {
                throw new EntityExistException("Profile with name %s already exist".formatted(profileDto.getName()));
            });
        }

        profile.setName(profileDto.getName());
        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId) {
        Profile profile = getById(profileId);
        profileRepository.delete(profile);
    }

    @Override
    public Profile getProfileByName(String name) {
        return profileRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("No profile with name: %s".formatted(name)));
    }

    @Override
    @Transactional
    public ProfileFractionsDto saveProfileWithFractions(String profileName, List<UploadFractionDto> uploadFractionDtos) {
        FractionsDto fractionsDto = convertUploadDtoToDto(uploadFractionDtos);
        fractionValidator.validateFractionValues(fractionsDto);

        Profile profile = profileRepository.findByName(profileName).orElseGet(() -> Profile.builder()
                .name(profileName)
                .build()
        );

        List<Fraction> fractions = fractionRepository.findAllByProfileName(profileName);

        if (CollectionUtils.isEmpty(fractions)) {
            fractions = fillFractionValues(fractionsDto, FractionUtil.createFractions(profile));
        } else {
            fractions = fillFractionValues(fractionsDto, fractions);
        }

        profileRepository.save(profile);
        fractionRepository.saveAll(fractions);

        return ProfileFractionsDto.builder()
                .id(profile.getId())
                .name(profileName)
                .fractionsDto(fractionsDto)
                .build();
    }
}