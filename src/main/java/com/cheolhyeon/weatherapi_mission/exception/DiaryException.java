package com.cheolhyeon.weatherapi_mission.exception;

import com.cheolhyeon.weatherapi_mission.type.ErrorCode;

public class DiaryException extends BaseException {
    public DiaryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
