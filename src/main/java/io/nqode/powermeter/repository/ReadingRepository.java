package io.nqode.powermeter.repository;

import io.nqode.powermeter.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    @Query("""
            select r from Reading r
                inner join r.meter m
            where m.id = :meterId
            """)
    List<Reading> findAllByMeterId(Long meterId);

    @Query("""
            select r from Reading r
                inner join r.meter m
                inner join m.profile p
            where p.id = :profileId 
            and m.id = :meterId
            """)
    List<Reading> findAllByProfileIdAndMeterId(Long profileId, Long meterId);

    @Query("""
            select r from Reading r
                inner join r.meter m
                inner join m.profile p
            where p.name = :profileName 
            and m.meterIdentifier = :meterIdentifier
            """)
    List<Reading> findAllByProfileIdAndMeterId(String profileName, String meterIdentifier);

}
