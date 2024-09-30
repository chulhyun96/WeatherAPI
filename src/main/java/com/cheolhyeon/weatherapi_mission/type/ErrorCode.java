package com.cheolhyeon.weatherapi_mission.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCESS_URL("URL 접근중 알 수 없는 오류 발생."),
    PARSING("데이터 변환중 오류가 발생했습니다."),
    DATE_NOT_FOUND("해당 날짜의 일기를 찾지 못하였습니다."),
    WEATHER_DOES_NOT_EXIST("요청하신 날짜의 날씨를 불러오지 못했습니다. 날짜를 확인해주세요"),
    LIST_IS_EMPTY("요청하신 날짜의 일기가 존재하지 않습니다.");

    private final String description;
}
