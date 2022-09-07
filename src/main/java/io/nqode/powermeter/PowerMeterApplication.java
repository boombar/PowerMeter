package io.nqode.powermeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@EnableWebMvc
@SpringBootApplication
public class PowerMeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerMeterApplication.class, args);
    }

}
