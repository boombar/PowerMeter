package io.nqode.powermeter.converter;

import io.nqode.powermeter.dto.MeterDto;
import io.nqode.powermeter.model.Meter;

public class MeterConverter {

    private MeterConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static MeterDto convertToDto(Meter meter) {
        return MeterDto.builder()
                .id(meter.getId())
                .meterIdentifier(meter.getMeterIdentifier())
                .build();
    }
}
