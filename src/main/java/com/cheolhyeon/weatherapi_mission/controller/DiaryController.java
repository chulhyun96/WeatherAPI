package com.cheolhyeon.weatherapi_mission.controller;

import com.cheolhyeon.weatherapi_mission.aop.RequestDateLogging;
import com.cheolhyeon.weatherapi_mission.domain.Diary;
import com.cheolhyeon.weatherapi_mission.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;


    @PostMapping("/create/diary")
    @RequestDateLogging
    public ResponseEntity<Diary> createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date, @RequestBody String text) {
        Diary diary = diaryService.createDiary(date, text);
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/read/diary")
    @Operation(summary = "파라미터로 들어온 날짜 읽어오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Diary.class))}),
            @ApiResponse(responseCode = "404", description = "해당 날짜가 아직 DB에 없는것임!!"),
    })
    public ResponseEntity<List<Diary>> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "날짜 형식 : yyyy-MM-dd", example = "2020-04-03") LocalDate date) {
        List<Diary> diaries = diaryService.readDiary(date);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/read/diaries")
    public ResponseEntity<List<Diary>> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {
        List<Diary> diaries = diaryService.readDiaries(startDate, endDate);
        return ResponseEntity.ok(diaries);
    }
    @PutMapping("/update/diary")
    public ResponseEntity<Diary> updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date, @RequestBody String updateText) {
        Diary diary = diaryService.updateDiary(date, updateText);
        return ResponseEntity.ok(diary);
    }

    @DeleteMapping("/delete/diary")
    public ResponseEntity<Integer> deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        Integer count = diaryService.deleteDiary(date);
        return ResponseEntity.ok(count);
    }
}
