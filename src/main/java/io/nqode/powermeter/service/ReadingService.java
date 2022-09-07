package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.model.Reading;

import java.util.List;

public interface ReadingService {
    List<Reading> getAllByProfileIdAndMeterId(Long profileId, Long meterId);

    List<Reading> createReadings(Long profileId, Long meterId, ReadingsDto readingsDto);

    List<Reading> updateReadings(Long profileId, Long meterId, ReadingsDto readingsDto);

    void deleteReadings(Long profileId, Long meterId);

}
