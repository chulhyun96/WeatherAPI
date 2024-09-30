package com.cheolhyeon.weatherapi_mission.repository;


import com.cheolhyeon.weatherapi_mission.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByDate(LocalDate date);
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<Diary> findFirstByDate(LocalDate date);
    Integer deleteAllByDate(LocalDate date);
}
