package io.nqode.powermeter.service;

import io.nqode.powermeter.converter.MeterConverter;
import io.nqode.powermeter.dto.MeterDto;
import io.nqode.powermeter.exception.EntityExistException;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.repository.MeterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MeterServiceTest extends BaseUniTest {

    private static final Long METER_ID = 1L;
    private static final String METER_IDENTIFIER = "M1";

    @Mock
    private MeterRepository meterRepository;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private MeterServiceImpl meterService;

    @Test
    void shouldGetAllByProfileId() {
        List<Meter> meters = List.of(
                createMeter(),
                createMeter()
        );

        when(meterRepository.findAllByProfileId(PROFILE_ID)).thenReturn(meters);

        List<Meter> response = meterService.getAllByProfileId(PROFILE_ID);

        assertTrue(response.containsAll(meters));
        assertEquals(meters.size(), response.size());

        verify(meterRepository).findAllByProfileId(PROFILE_ID);
    }

    @Test
    void shouldGetByProfileIdAndId() {
        Meter meter = createMeter();

        when(meterRepository.findByProfileIdAndMeterId(PROFILE_ID, meter.getId())).thenReturn(Optional.of(meter));

        Meter response = meterService.getByProfileIdAndId(PROFILE_ID, meter.getId());

        assertEquals(response, meter);

        verify(meterRepository).findByProfileIdAndMeterId(PROFILE_ID, meter.getId());
    }

    @Test
    void shouldCreateMeterForProfile() {
        Meter meter = createMeter();

        when(profileService.getById(any())).thenReturn(createProfile());
        when(meterRepository.findByMeterIdentifier(any())).thenReturn(Optional.empty());

        when(meterRepository.save(any())).thenReturn(meter);

        Meter meterForProfile = meterService.createMeterForProfile(PROFILE_ID, MeterConverter.convertToDto(meter));

        assertEquals(meter, meterForProfile);

        verify(profileService).getById(PROFILE_ID);
        verify(meterRepository).findByMeterIdentifier(METER_IDENTIFIER);
        verify(meterRepository).save(any());
    }


    @Test
    void shouldCreateMeterForProfile_thenEntityExistException() {
        Meter meter = createMeter();

        when(profileService.getById(any())).thenReturn(createProfile());
        when(meterRepository.findByMeterIdentifier(any())).thenReturn(Optional.of(meter));

        Exception exception = assertThrows(EntityExistException.class,
                () -> meterService.createMeterForProfile(PROFILE_ID, MeterConverter.convertToDto(meter)));

        assertTrue(exception.getMessage().contains("already exist"));
    }

    @Test
    void shouldUpdateMeter() {
        Meter meter = createMeter();

        MeterDto meterDto = MeterConverter.convertToDto(meter);
        meterDto.setMeterIdentifier("Updated-meter-identifier");

        when(meterRepository.findByProfileIdAndMeterId(any(), any())).thenReturn(Optional.of(meter));
        when(meterRepository.findByMeterIdentifier(any())).thenReturn(Optional.empty());
        when(meterRepository.save(any())).thenReturn(meter);

        Meter updateMeter = meterService.updateMeter(PROFILE_ID, METER_ID, meterDto);

        assertEquals(meterDto.getMeterIdentifier(), updateMeter.getMeterIdentifier());

        verify(meterRepository).findByProfileIdAndMeterId(PROFILE_ID, METER_ID);
        verify(meterRepository).findByMeterIdentifier(any());
        verify(meterRepository).save(any());
    }

    @Test
    void shouldDeleteMeter() {
        Meter meter = createMeter();
        when(meterRepository.findByProfileIdAndMeterId(any(), any())).thenReturn(Optional.of(meter));

        doNothing().when(meterRepository).delete(any());

        meterService.deleteMeter(PROFILE_ID, METER_ID);

        verify(meterRepository).findByProfileIdAndMeterId(PROFILE_ID, METER_ID);
        verify(meterRepository).delete(meter);
    }

    private Meter createMeter() {
        return Meter.builder()
                .id(METER_ID)
                .meterIdentifier(METER_IDENTIFIER)
                .profile(createProfile())
                .build();
    }
}
