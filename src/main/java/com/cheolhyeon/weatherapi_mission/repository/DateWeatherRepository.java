package com.cheolhyeon.weatherapi_mission.repository;

import com.cheolhyeon.weatherapi_mission.domain.DateWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, Long> {
    Optional<DateWeather> findByDate(LocalDate date);
}
