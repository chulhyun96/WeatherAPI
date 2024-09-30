package com.cheolhyeon.weatherapi_mission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DateWeather {
    @Id
    private LocalDate date;

    @NotNull
    @Size(max = 50)
    private String weather;

    @NotNull
    @Size(max = 50)
    private String icon;

    @NotNull
    private Double temperature;
}
