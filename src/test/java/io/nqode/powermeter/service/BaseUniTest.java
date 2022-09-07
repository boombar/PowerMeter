package io.nqode.powermeter.service;

import io.nqode.powermeter.model.Profile;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseUniTest {

    protected static final Long PROFILE_ID = 1L;

    protected Profile createProfile() {
        return Profile.builder()
                .id(PROFILE_ID)
                .name("Profile Name")
                .build();
    }
}
