package com.cheolhyeon.weatherapi_mission.controller;

import com.cheolhyeon.weatherapi_mission.domain.Diary;
import com.cheolhyeon.weatherapi_mission.service.DiaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.results.internal.StandardEntityGraphTraversalStateImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @MockBean
    DiaryService diaryService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private SpringDataWebAutoConfiguration springDataWebAutoConfiguration;

    @Test
    @DisplayName("다이어리 생성")
    void createDiary() throws Exception {
        //given
        Diary diary = Diary.builder()
                .weather("Clear")
                .icon("i0n")
                .temperature(249.00)
                .text("Hi")
                .date(LocalDate.now())
                .build();
        given(diaryService.createDiary(any(), anyString()))
                .willReturn(diary);
        //when
        ResultActions response = mockMvc.perform(
                post("/create/diary")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(diary)));
        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.weather")
                        .value("Clear"))
                .andExpect(jsonPath("$.icon")
                        .value("i0n"))
                .andExpect(jsonPath("$.temperature")
                        .value(249.00))
                .andExpect(jsonPath("$.text")
                        .value("Hi"))
                .andExpect(jsonPath("$.date")
                        .value(LocalDate.now().toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("startDate ~ endDate 별로 일기 불러오기")
    void readDiaries() throws Exception {
        //given
        List<Diary> list = IntStream.range(0, 5)
                .mapToObj(i -> Diary.builder()
                        .date(LocalDate.now().plusMonths(i)).build())
                .toList();

        LocalDate paramDate1 = list.get(0).getDate();
        LocalDate paramDate2 = list.get(4).getDate();

        given(diaryService.readDiaries(any(), any()))
                .willReturn(list);
        //when
        ResultActions response = mockMvc.perform(
                get("/read/diaries")
                        .param("startDate", paramDate1.toString())
                        .param("endDate", paramDate2.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(list)));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date")
                        .value(paramDate1.toString()))
                .andExpect(jsonPath("$[4].date")
                        .value(paramDate2.toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("date 검색으로 같은 날짜의 모든 일기 불러오기")
    void readDiary() throws Exception {
        //given
        List<Diary> list = List.of(Diary.builder()
                        .date(LocalDate.now())
                        .build(),
                Diary.builder()
                        .date(LocalDate.now())
                        .build(),
                Diary.builder()
                        .date(LocalDate.now())
                        .build());
        String paramDate = LocalDate.now().toString();
        given(diaryService.readDiary(any()))
                .willReturn(list);
        //when
        ResultActions response = mockMvc.perform(get("/read/diary")
                .param("date", paramDate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));
        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(paramDate))
                .andExpect(jsonPath("$[1].date").value(paramDate))
                .andExpect(jsonPath("$[2].date").value(paramDate))
                .andDo(print());

        assertEquals(list.size(), 3);
        assertEquals(list.get(0).getDate(), LocalDate.now());
    }

    @Test
    @DisplayName("다이어리 수정")
    void updateDiary() throws Exception {
        //given
        Diary updateDiary = Diary.builder()
                .text("UpdateText~")
                .date(LocalDate.now())
                .build();
        given(diaryService.updateDiary(any(), any()))
                .willReturn(updateDiary);
        //when
        ResultActions response = mockMvc.perform(
                put("/update/diary")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDiary.getText())));
        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.text")
                        .value("UpdateText~"))
                .andDo(print());
    }

    @Test
    @DisplayName("다이어리 삭제")
    void deleteDiary() throws Exception {
        //given
        List<Diary> deleteDiaries = List.of(Diary.builder()
                        .date(LocalDate.now())
                        .build(),
                Diary.builder()
                        .date(LocalDate.now())
                        .build(),
                Diary.builder()
                        .date(LocalDate.now())
                        .build());

        given(diaryService.deleteDiary(any()))
                .willReturn(deleteDiaries.size());
        //when
        mockMvc.perform(
                delete("/delete/diary")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                deleteDiaries.size()))
        );
        //then
        assertEquals(3, deleteDiaries.size());
    }

}