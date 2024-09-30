package com.cheolhyeon.weatherapi_mission.exception;

import com.cheolhyeon.weatherapi_mission.type.ErrorCode;

public class JsonParserException extends BaseException {
    public JsonParserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
