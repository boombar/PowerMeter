package io.nqode.powermeter.service;

import io.nqode.powermeter.dto.FractionsDto;
import io.nqode.powermeter.model.Fraction;

import java.util.List;

public interface FractionService {
    List<Fraction> getAllByProfileId(Long profileId);

    List<Fraction> createFractions(Long profileId, FractionsDto fractionsDto);

    List<Fraction> updateFractions(Long profileId, FractionsDto fractionsDto);

    void deleteFractions(Long profileId);
}
