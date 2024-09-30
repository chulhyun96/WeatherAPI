package com.cheolhyeon.weatherapi_mission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Diary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String weather;

    @NotNull
    @Size(max = 50)
    private String icon;

    @NotNull
    private Double temperature;

    @NotNull
    @Size(max = 500)
    private String text;

    private LocalDate date;

    public static Diary fromDateWeather(DateWeather dateWeather, String text) {
        return Diary.builder()
                .text(text)
                .weather(dateWeather.getWeather())
                .temperature(dateWeather.getTemperature())
                .icon(dateWeather.getIcon())
                .date(dateWeather.getDate())
                .build();
    }
}
