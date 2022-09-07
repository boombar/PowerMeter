package io.nqode.powermeter.controller;

import io.nqode.powermeter.dto.MeterDto;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.model.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeterControllerIntTest extends BaseIntTest {

    @Test
    void shouldGetMetersAndStatusOk() throws Exception {
        Meter meter = createMeter();

        mockMvc.perform(get(METER_API_PATH, meter.getProfile().getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(meter.getId()))
                .andExpect(jsonPath("$.[0].meterIdentifier").value(meter.getMeterIdentifier()))
                .andExpect(jsonPath("$.[1].id").doesNotExist());
    }

    @Test
    void shouldGetMeterAndStatusOk() throws Exception {
        Meter meter = createMeter();

        mockMvc.perform(get(METER_API_PATH + "/{id}", meter.getProfile().getId(), meter.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meter.getId()))
                .andExpect(jsonPath("$.meterIdentifier").value(meter.getMeterIdentifier()));
    }

    @Test
    void shouldPostMeterAndStatusCreated() throws Exception {
        Profile profile = createProfile();
        MeterDto meterDto = createMeterDto();

        mockMvc.perform(post(METER_API_PATH, profile.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meterDto))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meterIdentifier").value(meterDto.getMeterIdentifier()));
    }

    @Test
    void shouldDeleteMeterAndStatusOk() throws Exception {
        Meter meter = createMeter();

        mockMvc.perform(delete(METER_API_PATH + "/{id}", meter.getProfile().getId(), meter.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(meterRepository.existsById(meter.getId()));
    }

    private MeterDto createMeterDto() {
        return MeterDto.builder()
                .meterIdentifier("M43")
                .build();
    }
}
