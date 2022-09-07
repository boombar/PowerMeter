package io.nqode.powermeter.validation;

import io.nqode.powermeter.dto.ReadingsDto;
import io.nqode.powermeter.exception.EntityNotFoundException;
import io.nqode.powermeter.exception.ValidationException;
import io.nqode.powermeter.model.Fraction;
import io.nqode.powermeter.model.Reading;
import io.nqode.powermeter.service.FractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.nqode.powermeter.util.FractionUtil.LOWER_BOUND;
import static io.nqode.powermeter.util.FractionUtil.UPPER_BOUND;
import static io.nqode.powermeter.util.ReadingsUtil.*;

@Component
@RequiredArgsConstructor
public class ReadingValidator {

    private final FractionService fractionService;

    public void validateReadingValues(ReadingsDto readingsDto) {
        List<Reading> readings = fillReadingValues(readingsDto, createReadings(null));

        readings.sort(Comparator.comparing(Reading::getMonth));
        // sort list by readings value and compare
        if (!readings
                .stream()
                .sorted(Comparator.comparing(Reading::getValue))
                .toList()
                .equals(readings)) {
            throw new ValidationException("Readings are not valid, values not increasing");
        }
    }

    public void validateReadingsForProfileFractions(Long profileId, Long meterId, ReadingsDto readingsDto) {
        List<Fraction> fractions = fractionService.getAllByProfileId(profileId);
        if (CollectionUtils.isEmpty(fractions)) {
            throw new EntityNotFoundException("No fractions for profile: %d".formatted(profileId));
        }

        Map<Month, Integer> consumptionPerMonth = readingsToConsumption(
                fillReadingValues(readingsDto, createReadings(null)));
        Map<Month, BigDecimal> fractionPerMonth = fractions.stream()
                .collect(Collectors.toMap(Fraction::getMonth, Fraction::getFractionValue));

        // december reading is total consumption for that year
        BigDecimal totalConsumption = BigDecimal.valueOf(readingsDto.getDecemberReading());

        if (Arrays.stream(Month.values())
                .anyMatch(month -> isMonthlyConsumptionNotInLineWithFraction(consumptionPerMonth, fractionPerMonth, totalConsumption, month))) {
            throw new ValidationException("Meter %d readings are not in line with profile %d".formatted(meterId, profileId));
        }

    }

    private boolean isMonthlyConsumptionNotInLineWithFraction(Map<Month, Integer> consumptionPerMonth, Map<Month, BigDecimal> fractionPerMonth, BigDecimal totalConsumption, Month month) {
        BigDecimal consumption = BigDecimal.valueOf(consumptionPerMonth.get(month));
        BigDecimal fraction = fractionPerMonth.get(month);
        BigDecimal expectedConsumption = fraction.multiply(totalConsumption);
        BigDecimal upperBound = expectedConsumption.multiply(UPPER_BOUND);
        BigDecimal lowerBound = expectedConsumption.multiply(LOWER_BOUND);

        return consumption.compareTo(upperBound) > 0 || consumption.compareTo(lowerBound) < 0;
    }
}
