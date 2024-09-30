package com.cheolhyeon.weatherapi_mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherApiMissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherApiMissionApplication.class, args);
    }
}
