package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.exception.EntityExistException;
import io.nqode.powermeter.exception.EntityNotFoundException;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.model.Reading;
import io.nqode.powermeter.repository.MeterRepository;
import io.nqode.powermeter.repository.ReadingRepository;
import io.nqode.powermeter.util.ReadingsUtil;
import io.nqode.powermeter.validation.ReadingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static io.nqode.powermeter.util.ReadingsUtil.fillReadingValues;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

    private static final String READINGS_EXIST = "Readings already exist for meter: %d";
    private static final String READINGS_DO_NOT_EXIST = "No readings exist for meter id: %d";

    private final ReadingRepository readingRepository;

    private final ProfileService profileService;

    private final MeterRepository meterRepository;

    private final MeterService meterService;

    private final ReadingValidator readingValidator;

    @Override
    public List<Reading> getAllByProfileIdAndMeterId(Long profileId, Long meterId) {
        return readingRepository.findAllByProfileIdAndMeterId(profileId, meterId);
    }

    @Override
    @Transactional
    public List<Reading> createReadings(Long profileId, Long meterId, ReadingsDto readingsDto) {
        readingValidator.validateReadingValues(readingsDto);
        if (!CollectionUtils.isEmpty(getAllByProfileIdAndMeterId(profileId, meterId))) {
            throw new EntityExistException(READINGS_EXIST.formatted(meterId));
        }
        Meter meter = meterService.getByProfileIdAndId(profileId, meterId);

        readingValidator.validateReadingsForProfileFractions(profileId, meterId, readingsDto);
        return readingRepository.saveAll(fillReadingValues(readingsDto, ReadingsUtil.createReadings(meter)));
    }

    @Override
    @Transactional
    public List<Reading> updateReadings(Long profileId, Long meterId, ReadingsDto readingsDto) {
        readingValidator.validateReadingValues(readingsDto);
        List<Reading> readings = getAllByProfileIdAndMeterId(profileId, meterId);
        if (CollectionUtils.isEmpty(readings)) {
            throw new EntityNotFoundException(READINGS_DO_NOT_EXIST.formatted(meterId));
        }
        readingValidator.validateReadingsForProfileFractions(profileId, meterId, readingsDto);
        return readingRepository.saveAll(fillReadingValues(readingsDto, readings));
    }

    @Override
    @Transactional
    public void deleteReadings(Long profileId, Long meterId) {
        List<Reading> readings = getAllByProfileIdAndMeterId(profileId, meterId);
        if (CollectionUtils.isEmpty(readings)) {
            throw new EntityNotFoundException(READINGS_DO_NOT_EXIST.formatted(meterId));
        }
        readingRepository.deleteAll(readings);
    }
}
