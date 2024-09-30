package com.cheolhyeon.weatherapi_mission.exception;


import com.cheolhyeon.weatherapi_mission.type.ErrorCode;

public class URLException extends BaseException {
    public URLException(ErrorCode errorCode) {
        super(errorCode);
    }
}
