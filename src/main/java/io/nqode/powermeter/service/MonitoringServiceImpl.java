package io.nqode.powermeter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static io.nqode.powermeter.service.LegacyServiceImpl.UPLOAD_LOG;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements ApplicationListener<ApplicationReadyEvent> {

    private final WatchService watchService;

    private final LegacyService legacyService;

    @Async
    public void launchMonitoring() {
        log.info("START_MONITORING");
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    String filename = event.context().toString();
                    log.debug("Event kind: {}; File affected: {}", event.kind(), filename);
                    if (!filename.equals(UPLOAD_LOG)) {
                        legacyService.parseCSVFromFile(event.context().toString());
                    }
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        log.info("STOP_MONITORING");

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }
    }

    // start monitoring only after context is loaded
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("Application ready");
        launchMonitoring();
    }

}
