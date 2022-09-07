package io.nqode.powermeter.service;

import io.nqode.powermeter.converter.ReadingConverter;
import io.nqode.powermeter.dto.*;
import io.nqode.powermeter.exception.EntityExistException;
import io.nqode.powermeter.exception.EntityNotFoundException;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.model.Profile;
import io.nqode.powermeter.model.Reading;
import io.nqode.powermeter.repository.MeterRepository;
import io.nqode.powermeter.repository.ReadingRepository;
import io.nqode.powermeter.util.DateUtil;
import io.nqode.powermeter.util.ReadingsUtil;
import io.nqode.powermeter.validation.ReadingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.List;
import java.util.Optional;

import static io.nqode.powermeter.util.ReadingsUtil.fillReadingValues;
import static io.nqode.powermeter.util.ReadingsUtil.readingsToConsumption;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;

    private final ReadingRepository readingRepository;

    private final ProfileService profileService;

    private final ReadingValidator readingValidator;

    @Override
    public List<Meter> getAllByProfileId(Long profileId) {
        return meterRepository.findAllByProfileId(profileId);
    }

    @Override
    public Meter getByProfileIdAndId(Long profileId, Long id) {
        return meterRepository.findByProfileIdAndMeterId(profileId, id)
                .orElseThrow(() -> new EntityNotFoundException("No meter with id: %d".formatted(id)));
    }

    @Override
    @Transactional
    public Meter createMeterForProfile(Long profileId, MeterDto meterDto) {
        Profile profile = profileService.getById(profileId);
        meterRepository.findByMeterIdentifier(meterDto.getMeterIdentifier()).ifPresent(meter -> {
            throw new EntityExistException("Meter with meterIdentifier %s already exist".formatted(meterDto.getMeterIdentifier()));
        });

        return meterRepository.save(Meter.builder()
                .profile(profile)
                .meterIdentifier(meterDto.getMeterIdentifier())
                .build());
    }

    @Override
    @Transactional
    public Meter updateMeter(Long profileId, Long id, MeterDto meterDto) {
        Meter meter = getByProfileIdAndId(profileId, id);

        if (!meter.getMeterIdentifier().equals(meterDto.getMeterIdentifier())) {
            meterRepository.findByMeterIdentifier(meterDto.getMeterIdentifier()).ifPresent(foundMeter -> {
                throw new EntityExistException("Meter with meterIdentifier %s already exist".formatted(meterDto.getMeterIdentifier()));
            });
        }

        meter.setMeterIdentifier(meterDto.getMeterIdentifier());
        return meterRepository.save(meter);
    }

    @Override
    @Transactional
    public void deleteMeter(Long profileId, Long id) {
        Meter meter = getByProfileIdAndId(profileId, id);
        meterRepository.delete(meter);
    }

    @Override
    public Optional<Meter> findMeterByMeterIdentifier(String meterIdentifier) {
        return meterRepository.findByMeterIdentifier(meterIdentifier);
    }

    @Override
    public ConsumptionDto getMeterConsumptionFomMonth(Long profileId, Long meterId, String monthCode) {
        Meter meter = getByProfileIdAndId(profileId, meterId);
        Month month = DateUtil.getMonth(monthCode);
        List<Reading> readings = readingRepository.findAllByMeterId(meter.getId());
        if (CollectionUtils.isEmpty(readings)) {
            throw new EntityNotFoundException("No readings for meter %s".formatted(meterId));
        }
        return ConsumptionDto.builder()
                .consumption(readingsToConsumption(readings).get(month))
                .build();
    }

    @Override
    @Transactional
    public MeterReadingsDto saveUploadedMeterReadings(String profileName,
                                                      String meterIdentifier,
                                                      List<UploadReadingDto> uploadReadingDtos) {
        Profile profile = profileService.getProfileByName(profileName);
        Meter meter = findMeterByMeterIdentifier(meterIdentifier)
                .orElseGet(() -> Meter.builder()
                        .meterIdentifier(meterIdentifier)
                        .profile(profile)
                        .build());


        ReadingsDto readingsDto = ReadingConverter.convertUploadDtoToDto(uploadReadingDtos);
        readingValidator.validateReadingValues(readingsDto);
        readingValidator.validateReadingsForProfileFractions(profile.getId(), meter.getId(), readingsDto);

        List<Reading> readings = readingRepository.findAllByProfileIdAndMeterId(profileName, meterIdentifier);
        if (CollectionUtils.isEmpty(readings)) {
            readings = fillReadingValues(readingsDto, ReadingsUtil.createReadings(meter));
        } else {
            readings = fillReadingValues(readingsDto, readings);
        }

        meterRepository.save(meter);
        readingRepository.saveAll(readings);

        return MeterReadingsDto.builder()
                .id(meter.getId())
                .meterIdentifier(meterIdentifier)
                .readingsDto(readingsDto)
                .build();
    }
}
