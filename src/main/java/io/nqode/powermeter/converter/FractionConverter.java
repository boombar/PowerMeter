package io.nqode.powermeter.converter;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.dto.UploadFractionDto;
import io.nqode.powermeter.model.Fraction;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.nqode.powermeter.util.DateUtil.getMonth;

public class FractionConverter {

    private FractionConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static FractionsDto convertToDto(List<Fraction> fractions) {

        return fillFractionsDto(fractions.stream()
                .collect(Collectors.toMap(Fraction::getMonth, Fraction::getFractionValue)));
    }

    public static FractionsDto convertUploadDtoToDto(List<UploadFractionDto> uploadFractionDtos) {

        return fillFractionsDto(uploadFractionDtos.stream()
                .collect(Collectors.toMap(dto -> getMonth(dto.getMonth()), UploadFractionDto::getFraction)));

    }

    private static FractionsDto fillFractionsDto(Map<Month, BigDecimal> fractionPerMonth) {
        return FractionsDto.builder()
                .januaryFraction(fractionPerMonth.get(Month.JANUARY))
                .februaryFraction(fractionPerMonth.get(Month.FEBRUARY))
                .marchFraction(fractionPerMonth.get(Month.MARCH))
                .aprilFraction(fractionPerMonth.get(Month.APRIL))
                .mayFraction(fractionPerMonth.get(Month.MAY))
                .juneFraction(fractionPerMonth.get(Month.JUNE))
                .julyFraction(fractionPerMonth.get(Month.JULY))
                .augustFraction(fractionPerMonth.get(Month.AUGUST))
                .septemberFraction(fractionPerMonth.get(Month.SEPTEMBER))
                .octoberFraction(fractionPerMonth.get(Month.OCTOBER))
                .novemberFraction(fractionPerMonth.get(Month.NOVEMBER))
                .decemberFraction(fractionPerMonth.get(Month.DECEMBER))
                .build();
    }
}
