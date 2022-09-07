package io.nqode.powermeter.util;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.model.Fraction;
import io.nqode.powermeter.model.Profile;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.Month.*;

public class FractionUtil {

    private FractionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final BigDecimal FRACTION_SUM_TOTAL = BigDecimal.ONE;
    public static final BigDecimal TOLERANCE = new BigDecimal("0.25");
    public static final BigDecimal UPPER_BOUND = BigDecimal.ONE.add(TOLERANCE);
    public static final BigDecimal LOWER_BOUND = BigDecimal.ONE.subtract(TOLERANCE);

    public static List<Fraction> fillFractionValues(FractionsDto fractionsDto, List<Fraction> fractions) {
        Map<Month, Fraction> fractionsPerMonth = fractions.stream()
                .collect(Collectors.toMap(Fraction::getMonth, Function.identity()));

        fractionsPerMonth.get(JANUARY).setFractionValue(fractionsDto.getJanuaryFraction());
        fractionsPerMonth.get(FEBRUARY).setFractionValue(fractionsDto.getFebruaryFraction());
        fractionsPerMonth.get(MARCH).setFractionValue(fractionsDto.getMarchFraction());
        fractionsPerMonth.get(APRIL).setFractionValue(fractionsDto.getAprilFraction());
        fractionsPerMonth.get(MAY).setFractionValue(fractionsDto.getMayFraction());
        fractionsPerMonth.get(JUNE).setFractionValue(fractionsDto.getJuneFraction());
        fractionsPerMonth.get(JULY).setFractionValue(fractionsDto.getJulyFraction());
        fractionsPerMonth.get(AUGUST).setFractionValue(fractionsDto.getAugustFraction());
        fractionsPerMonth.get(SEPTEMBER).setFractionValue(fractionsDto.getSeptemberFraction());
        fractionsPerMonth.get(OCTOBER).setFractionValue(fractionsDto.getOctoberFraction());
        fractionsPerMonth.get(NOVEMBER).setFractionValue(fractionsDto.getNovemberFraction());
        fractionsPerMonth.get(DECEMBER).setFractionValue(fractionsDto.getDecemberFraction());

        return new ArrayList<>(fractionsPerMonth.values());

    }

    public static List<Fraction> createFractions(Profile profile) {
        return Arrays.stream(values())
                .map(month -> Fraction
                        .builder()
                        .month(month)
                        .profile(profile)
                        .build())
                .toList();
    }
}
