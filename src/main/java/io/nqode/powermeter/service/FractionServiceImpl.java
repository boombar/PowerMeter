package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.exception.EntityExistException;
import io.nqode.powermeter.exception.EntityNotFoundException;
import io.nqode.powermeter.model.Fraction;
import io.nqode.powermeter.model.Profile;
import io.nqode.powermeter.repository.FractionRepository;
import io.nqode.powermeter.util.FractionUtil;
import io.nqode.powermeter.validation.FractionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static io.nqode.powermeter.util.FractionUtil.fillFractionValues;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FractionServiceImpl implements FractionService {

    private static final String FRACTIONS_EXIST = "Fractions already exist for profile: %d";
    private static final String FRACTIONS_DO_NOT_EXIST = "No fractions exist for profile: %d";

    private final FractionRepository fractionRepository;

    private final ProfileService profileService;

    private final FractionValidator fractionValidator;

    @Override
    public List<Fraction> getAllByProfileId(Long profileId) {
        return fractionRepository.findAllByProfileId(profileId);
    }

    @Override
    @Transactional
    public List<Fraction> createFractions(Long profileId, FractionsDto fractionsDto) {
        fractionValidator.validateFractionValues(fractionsDto);
        Profile profile = profileService.getById(profileId);
        if (!CollectionUtils.isEmpty(this.getAllByProfileId(profileId))) {
            throw new EntityExistException(FRACTIONS_EXIST.formatted(profileId));
        }
        return fractionRepository.saveAll(fillFractionValues(fractionsDto, FractionUtil.createFractions(profile)));
    }

    @Override
    @Transactional
    public List<Fraction> updateFractions(Long profileId, FractionsDto fractionsDto) {
        fractionValidator.validateFractionValues(fractionsDto);
        Profile profile = profileService.getById(profileId);
        List<Fraction> fractions = this.getAllByProfileId(profile.getId());
        if (CollectionUtils.isEmpty(fractions)) {
            throw new EntityExistException(FRACTIONS_DO_NOT_EXIST.formatted(profileId));
        }
        return fractionRepository.saveAll(fillFractionValues(fractionsDto, fractions));
    }

    @Override
    @Transactional
    public void deleteFractions(Long profileId) {
        List<Fraction> fractions = this.getAllByProfileId(profileId);
        if (CollectionUtils.isEmpty(fractions)) {
            throw new EntityNotFoundException(FRACTIONS_DO_NOT_EXIST.formatted(profileId));
        }
        fractionRepository.deleteAll(fractions);
    }
}
