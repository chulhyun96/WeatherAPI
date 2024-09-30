package com.cheolhyeon.weatherapi_mission.service;

import com.cheolhyeon.weatherapi_mission.domain.DateWeather;
import com.cheolhyeon.weatherapi_mission.domain.Diary;
import com.cheolhyeon.weatherapi_mission.exception.DiaryException;
import com.cheolhyeon.weatherapi_mission.repository.DateWeatherRepository;
import com.cheolhyeon.weatherapi_mission.repository.DiaryRepository;
import com.cheolhyeon.weatherapi_mission.type.ErrorCode;
import org.hibernate.annotations.ManyToAny;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    DiaryRepository diaryRepository;

    @Mock
    DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    DiaryService diaryService;

    @Test
    @DisplayName("다이어리 생성 - 성공")
    public void createDiary_Success() {
        // given
        LocalDate date = LocalDate.now();
        String text = "Create Diary Test";

        DateWeather mockDateWeather = DateWeather.builder()
                .date(date)
                .weather("Clear")
                .icon("i0n")
                .temperature(293.0)
                .build();

        given(dateWeatherRepository.findByDate(any()))
                .willReturn(Optional.of(mockDateWeather));

        Diary mockDiary = Diary.fromDateWeather(mockDateWeather, text);

        given(diaryRepository.save(any(Diary.class)))
                .willReturn(mockDiary);

        //when
        Diary newDiary = diaryService.createDiary(date, text);

        //then
        verify(dateWeatherRepository, times(1)).findByDate(any());
        verify(diaryRepository, times(1)).save(any(Diary.class));

        assertNotNull(newDiary);
        assertEquals(mockDateWeather.getWeather(), newDiary.getWeather());
        assertEquals(mockDiary.getText(), newDiary.getText());
    }

    @Test
    @DisplayName("다이어리 생성 - 실패 (해당 날짜의 날씨 데이터가 없는 경우)")
    public void createDiary_Fail() {
        // given
        LocalDate date = LocalDate.now();
        String text = "Create Diary Test";

        given(dateWeatherRepository.findByDate(any()))
                .willReturn(Optional.empty());

        //when
        DiaryException exception = assertThrows(
                DiaryException.class, () -> diaryService.createDiary(date, text)
        );
        //then
        assertEquals(exception.getErrorCode(), ErrorCode.WEATHER_DOES_NOT_EXIST);
        verify(dateWeatherRepository, times(1))
                .findByDate(any());
        verify(diaryRepository, times(0))
                .save(any(Diary.class));
    }

    @Test
    @DisplayName("같은 날짜의 데이터 조회 - 성공")
    void readDiary_Success() {
        //given
        LocalDate date = LocalDate.now();
        List<Diary> list = List.of(Diary.builder()
                        .date(date)
                        .build(),
                Diary.builder()
                        .date(date)
                        .build(),
                Diary.builder()
                        .date(date)
                        .build());
        given(diaryRepository.findAllByDate(any()))
                .willReturn(list);
        //when
        List<Diary> diaries = diaryService.readDiary(date);
        //then
        assertNotNull(diaries);
        assertEquals(diaries.size(), list.size());
    }
    @Test
    @DisplayName("같은 날짜의 데이터 조회 - 실패")
    void readDiary_Fail() {
        //given
        LocalDate date = LocalDate.now();
        List<Diary> list = List.of();
        given(diaryRepository.findAllByDate(any()))
                .willReturn(list);
        //when
        DiaryException exception = assertThrows(
                DiaryException.class, () -> diaryService.readDiary(date));
        //then;
        verify(diaryRepository, times(1)).findAllByDate(any());
        assertEquals(0, list.size());
        assertEquals(exception.getErrorCode(), ErrorCode.LIST_IS_EMPTY);
    }
    @Test
    @DisplayName("startDate ~ endDate 조회 - 성공")
    void readDiaries_Success() {
        //given
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Diary> list = List.of(Diary.builder()
                        .date(startDate)
                        .build(),
                Diary.builder()
                        .date(endDate)
                        .build());

        given(diaryRepository.findAllByDateBetween(any(), any()))
                .willReturn(list);
        //when
        List<Diary> diaries = diaryService.readDiaries(startDate, endDate);
        //then
        assertNotNull(diaries);
        assertEquals(diaries.size(), list.size());
    }
    @Test
    @DisplayName("startDate ~ endDate 조회 - 실패")
    void readDiaries_Fail() {
        //given
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Diary> list = List.of();

        given(diaryRepository.findAllByDateBetween(any(), any()))
                .willReturn(list);
        //when
        DiaryException exception = assertThrows(
                DiaryException.class, () -> diaryService.readDiaries(startDate, endDate));
        //then
        assertEquals(exception.getErrorCode(), ErrorCode.LIST_IS_EMPTY);
    }
    @Test
    @DisplayName("다이어리 업데이트 - 성공")
    void updateDiary_Success() {
        //given
        LocalDate preDate = LocalDate.now().minusMonths(1);
        String preText = "Previous Diary Text";
        Diary preDiary = Diary.builder()
                .date(preDate)
                .text(preText)
                .build();

        String currentText = "Update Diary Test";

        given(diaryRepository.findFirstByDate(any()))
                .willReturn(Optional.of(preDiary));
        //when
        Diary updateDiary = diaryService.updateDiary(preDate, currentText);
        //then
        verify(diaryRepository, times(1)).findFirstByDate(any());
        assertEquals(updateDiary.getText(), currentText);
        assertEquals(updateDiary.getDate(), preDate);
    }
    @Test
    @DisplayName("다이어리 업데이트 - 실패")
    void updateDiary_Fail() {
        //given
        LocalDate preDate = LocalDate.now().minusMonths(1);
        given(diaryRepository.findFirstByDate(any()))
                .willReturn(Optional.empty());
        //when
        assertThrows(DiaryException.class,
                () -> diaryService.updateDiary(preDate, "Update Diary Test"));

        //then
        verify(diaryRepository, times(1))
                .findFirstByDate(any());
    }
    @Test
    @DisplayName("다이어리 삭제 - 성공")
    void deleteDiary_Success() {
        //given
        Diary diary = Diary.builder()
                .date(LocalDate.now())
                .text("Delete Diary")
                .build();
        given(diaryRepository.deleteAllByDate(any()))
                .willReturn(1);
        //when
        Integer count = diaryService.deleteDiary(diary.getDate());
        //then
        assertNotNull(count);
        assertEquals(1, count);
        verify(diaryRepository, times(1)).deleteAllByDate(any());
    }
    @Test
    @DisplayName("다이어리 삭제 - 실패")
    void deleteDiary_Fail() {
        //given
        given(diaryRepository.deleteAllByDate(any()))
                .willReturn(null);
        //when
        DiaryException exception = assertThrows(DiaryException.class,
                () -> diaryService.deleteDiary(LocalDate.now()));
        //then
        assertEquals(exception.getErrorCode(), ErrorCode.DATE_NOT_FOUND);
        verify(diaryRepository, times(1)).deleteAllByDate(any());
    }
}