package io.nqode.powermeter.repository;

import io.nqode.powermeter.model.Fraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FractionRepository extends JpaRepository<Fraction, Long> {

    @Query("""
            select f from Fraction f
                inner join f.profile p
            where p.id = :profileId
            """)
    List<Fraction> findAllByProfileId(Long profileId);


    @Query("""
            select f from Fraction f
                inner join f.profile p
            where p.name = :profileName
            """)
    List<Fraction> findAllByProfileName(String profileName);

}
