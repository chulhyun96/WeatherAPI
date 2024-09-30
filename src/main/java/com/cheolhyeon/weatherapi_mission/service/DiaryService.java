package com.cheolhyeon.weatherapi_mission.service;

import com.cheolhyeon.weatherapi_mission.aop.RequestDateLogging;
import com.cheolhyeon.weatherapi_mission.domain.DateWeather;
import com.cheolhyeon.weatherapi_mission.domain.Diary;
import com.cheolhyeon.weatherapi_mission.exception.DiaryException;
import com.cheolhyeon.weatherapi_mission.exception.JsonParserException;
import com.cheolhyeon.weatherapi_mission.exception.URLException;
import com.cheolhyeon.weatherapi_mission.repository.DateWeatherRepository;
import com.cheolhyeon.weatherapi_mission.repository.DiaryRepository;
import com.cheolhyeon.weatherapi_mission.type.ErrorCode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    @RequestDateLogging
    public void saveDateWeather() {
        dateWeatherRepository.save(getDateWeather());
    }

    private DateWeather getDateWeather() {
        String weatherJsonString = getWeatherFromApi();
        Map<String, Object> parsedWeather = parseWeatherToJson(weatherJsonString);
        return DateWeather.builder()
                .date(LocalDate.now())
                .weather(parsedWeather.get("main").toString())
                .temperature((Double) parsedWeather.get("temp"))
                .icon(parsedWeather.get("icon").toString())
                .build();
    }

    @Transactional
    public Diary createDiary(LocalDate date, String text) {
        //TODO : DB에서 날씨 데이터 가져오기
        DateWeather dateWeather = dateWeatherRepository.findByDate(date)
                .orElseThrow(() -> new DiaryException(ErrorCode.WEATHER_DOES_NOT_EXIST));
        //TODO : JSON으로 파싱된 데이터 + 일기 text 값 DB에 Save
        Diary diary = Diary.fromDateWeather(dateWeather, text);
        return diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        List<Diary> allByDate = diaryRepository.findAllByDate(date);
        validationByDate(allByDate);
        return allByDate;
    }

    private void validationByDate(List<Diary> allByDate) {
        if (allByDate.isEmpty()) {
            throw new DiaryException(ErrorCode.LIST_IS_EMPTY);
        }
    }

    @Transactional
    public Diary updateDiary(LocalDate date, String updateText) {
        Diary findDiary = diaryRepository.findFirstByDate(date).orElseThrow(
                () -> new DiaryException(ErrorCode.DATE_NOT_FOUND));
        findDiary.setText(updateText);
        return findDiary;
    }

    @Transactional
    public Integer deleteDiary(LocalDate date) {
        Integer count = diaryRepository.deleteAllByDate(date);
        if (count == null) {
            throw new DiaryException(ErrorCode.DATE_NOT_FOUND);
        }
        return count;
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        List<Diary> allByDateBetween = diaryRepository.findAllByDateBetween(startDate, endDate);
        validationByDate(allByDateBetween);
        return allByDateBetween;
    }

    private Map<String, Object> parseWeatherToJson(String weatherJsonString) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject;
        try {
            jsonObject = jsonParser.parse(weatherJsonString).getAsJsonObject();
        } catch (JsonParserException e) {
            throw new JsonParserException(ErrorCode.PARSING);
        }
        Map<String, Object> resultMap = new HashMap<>();
        JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
        JsonElement weatherData = weatherArray.get(0).getAsJsonObject().get("main");
        JsonElement weatherIconData = weatherArray.get(0).getAsJsonObject().get("icon");
        double tempData = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        resultMap.put("temp", tempData);
        resultMap.put("main", weatherData);
        resultMap.put("icon", weatherIconData);
        return resultMap;
    }

    private String getWeatherFromApi() {
        final String SEOUL_WEATHER = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try {
            BufferedReader br = getWeatherData(SEOUL_WEATHER);
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            throw new URLException(ErrorCode.ACCESS_URL);
        }
    }

    private BufferedReader getWeatherData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // 데이터를 조회 및 가져오는 거기 때문에 GET
        int responseCode = connection.getResponseCode();// 받아온 응답 결과의 Http Code를 가지고옴 -> 200,201...

        BufferedReader br;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            log.info("HTTP API 데이터 가져오기");
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        return br;
    }
}
