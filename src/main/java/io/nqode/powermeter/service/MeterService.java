package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.ConsumptionDto;
import io.nqode.powermeter.dto.MeterDto;
import io.nqode.powermeter.dto.MeterReadingsDto;
import io.nqode.powermeter.dto.UploadReadingDto;
import io.nqode.powermeter.model.Meter;

import java.util.List;
import java.util.Optional;

public interface MeterService {

    List<Meter> getAllByProfileId(Long profileId);

    Meter getByProfileIdAndId(Long profileId, Long id);

    Meter createMeterForProfile(Long profileId, MeterDto meterDto);

    Meter updateMeter(Long profileId, Long id, MeterDto meterDto);

    void deleteMeter(Long profileId, Long id);

    ConsumptionDto getMeterConsumptionFomMonth(Long profileId, Long meterId, String month);

    Optional<Meter> findMeterByMeterIdentifier(String meterIdentifier);

    MeterReadingsDto saveUploadedMeterReadings(String profileName, String meterIdentifier, List<UploadReadingDto> readings);
}

