package io.nqode.powermeter.repository;

import io.nqode.powermeter.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

    Optional<Meter> findByMeterIdentifier(String meterIdentifier);

    @Query("""
            select m from Meter m
                inner join m.profile p
            where p.id = :profileName
                and m.meterIdentifier = :meterIdentifier                
            """)
    Optional<Meter> findByProfileNameAndMeterIdentifier(String profileName, String meterIdentifier);

    @Query("""
            select m from Meter m
                inner join m.profile p
            where p.id = :profileId                  
            """)
    List<Meter> findAllByProfileId(Long profileId);

    @Query("""
            select m from Meter m
                inner join m.profile p
            where p.id = :profileId
                and m.id = :meterId                  
            """)
    Optional<Meter> findByProfileIdAndMeterId(Long profileId, Long meterId);

}
