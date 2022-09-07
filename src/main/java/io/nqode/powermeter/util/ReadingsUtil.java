package io.nqode.powermeter.util;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.model.Reading;

import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.Month.values;

public class ReadingsUtil {

    private ReadingsUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<Month, Integer> readingsToConsumption(List<Reading> readings) {
        Map<Month, Integer> consumptionPerMonth = new EnumMap<>(Month.class);
        readings.sort(Comparator.comparing(Reading::getMonth));
        consumptionPerMonth.put(readings.get(0).getMonth(), readings.get(0).getValue());
        for (int i = 1, readingsSize = readings.size(); i < readingsSize; i++) {
            Reading reading = readings.get(i);
            consumptionPerMonth.put(reading.getMonth(), reading.getValue() - readings.get(i - 1).getValue());
        }
        return consumptionPerMonth;
    }

    public static List<Reading> createReadings(Meter meter) {
        return Arrays.stream(values())
                .map(month -> Reading
                        .builder()
                        .month(month)
                        .meter(meter)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Reading> fillReadingValues(ReadingsDto readingsDto, List<Reading> readings) {
        Map<Month, Reading> readingPerMonth = readings.stream()
                .collect(Collectors.toMap(Reading::getMonth, Function.identity()));

        readingPerMonth.get(Month.JANUARY).setValue(readingsDto.getJanuaryReading());
        readingPerMonth.get(Month.FEBRUARY).setValue(readingsDto.getFebruaryReading());
        readingPerMonth.get(Month.MARCH).setValue(readingsDto.getMarchReading());
        readingPerMonth.get(Month.APRIL).setValue(readingsDto.getAprilReading());
        readingPerMonth.get(Month.MAY).setValue(readingsDto.getMayReading());
        readingPerMonth.get(Month.JUNE).setValue(readingsDto.getJuneReading());
        readingPerMonth.get(Month.JULY).setValue(readingsDto.getJulyReading());
        readingPerMonth.get(Month.AUGUST).setValue(readingsDto.getAugustReading());
        readingPerMonth.get(Month.SEPTEMBER).setValue(readingsDto.getSeptemberReading());
        readingPerMonth.get(Month.OCTOBER).setValue(readingsDto.getOctoberReading());
        readingPerMonth.get(Month.NOVEMBER).setValue(readingsDto.getNovemberReading());
        readingPerMonth.get(Month.DECEMBER).setValue(readingsDto.getDecemberReading());

        return new ArrayList<>(readingPerMonth.values());
    }

}
