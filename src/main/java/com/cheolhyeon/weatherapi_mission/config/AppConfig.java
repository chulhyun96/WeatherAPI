package com.cheolhyeon.weatherapi_mission.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {
    @PostConstruct
    public void printJavaVersion() {
        String javaVersion = System.getProperty("java.version");

        log.info("Java version: {}", javaVersion);
    }
}
