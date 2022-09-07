package io.nqode.powermeter.validation;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static io.nqode.powermeter.util.FractionUtil.FRACTION_SUM_TOTAL;

@Component
public class FractionValidator {

    public void validateFractionValues(FractionsDto fractionsDto) {
        BigDecimal fractionSum = BigDecimal.ZERO;
        fractionSum = fractionSum.add(fractionsDto.getJanuaryFraction());
        fractionSum = fractionSum.add(fractionsDto.getFebruaryFraction());
        fractionSum = fractionSum.add(fractionsDto.getMarchFraction());
        fractionSum = fractionSum.add(fractionsDto.getAprilFraction());
        fractionSum = fractionSum.add(fractionsDto.getMayFraction());
        fractionSum = fractionSum.add(fractionsDto.getJuneFraction());
        fractionSum = fractionSum.add(fractionsDto.getJulyFraction());
        fractionSum = fractionSum.add(fractionsDto.getAugustFraction());
        fractionSum = fractionSum.add(fractionsDto.getSeptemberFraction());
        fractionSum = fractionSum.add(fractionsDto.getOctoberFraction());
        fractionSum = fractionSum.add(fractionsDto.getNovemberFraction());
        fractionSum = fractionSum.add(fractionsDto.getDecemberFraction());

        if (fractionSum.compareTo(FRACTION_SUM_TOTAL) != 0) {
            throw new ValidationException("Fraction sum is not %f".formatted(FRACTION_SUM_TOTAL));
        }
    }
}
