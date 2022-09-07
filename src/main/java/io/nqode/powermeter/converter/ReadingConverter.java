package io.nqode.powermeter.converter;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.dto.UploadReadingDto;
import io.nqode.powermeter.model.Reading;
import io.nqode.powermeter.util.DateUtil;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadingConverter {

    private ReadingConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static ReadingsDto convertToDto(List<Reading> readings) {

        Map<Month, Integer> readingPerMonth = readings.stream()
                .collect(Collectors.toMap(Reading::getMonth, Reading::getValue));

        return getReadingsDto(readingPerMonth);
    }

    public static ReadingsDto convertUploadDtoToDto(List<UploadReadingDto> uploadDtos) {

        Map<Month, Integer> readingPerMonth = uploadDtos.stream()
                .collect(Collectors.toMap(dto -> DateUtil.getMonth(dto.getMonth()), UploadReadingDto::getMeterReading));

        return getReadingsDto(readingPerMonth);
    }

    private static ReadingsDto getReadingsDto(Map<Month, Integer> readingPerMonth) {
        return ReadingsDto.builder()
                .januaryReading(readingPerMonth.get(Month.JANUARY))
                .februaryReading(readingPerMonth.get(Month.FEBRUARY))
                .marchReading(readingPerMonth.get(Month.MARCH))
                .aprilReading(readingPerMonth.get(Month.APRIL))
                .mayReading(readingPerMonth.get(Month.MAY))
                .juneReading(readingPerMonth.get(Month.JUNE))
                .julyReading(readingPerMonth.get(Month.JULY))
                .augustReading(readingPerMonth.get(Month.AUGUST))
                .septemberReading(readingPerMonth.get(Month.SEPTEMBER))
                .octoberReading(readingPerMonth.get(Month.OCTOBER))
                .novemberReading(readingPerMonth.get(Month.NOVEMBER))
                .decemberReading(readingPerMonth.get(Month.DECEMBER))
                .build();
    }

}
