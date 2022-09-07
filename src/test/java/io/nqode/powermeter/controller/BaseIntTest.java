package io.nqode.powermeter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nqode.powermeter.model.Meter;
import io.nqode.powermeter.model.Profile;
import io.nqode.powermeter.repository.MeterRepository;
import io.nqode.powermeter.repository.ProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.WatchService;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseIntTest {

    protected static final String METER_API_PATH = "/profiles/{profileId}/meters";

    @MockBean
    private WatchService watchService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MeterRepository meterRepository;

    @Autowired
    private ProfileRepository profileRepository;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    protected void cleanUp() {
        meterRepository.deleteAll();
        profileRepository.deleteAll();
    }

    protected Meter createMeter() {
        return meterRepository.save(Meter.builder()
                .meterIdentifier("M1")
                .profile(createProfile())
                .readings(Collections.emptyList())
                .build());
    }

    protected Profile createProfile() {
        return profileRepository.save(Profile.builder()
                .name("Profile")
                .build());
    }
}
