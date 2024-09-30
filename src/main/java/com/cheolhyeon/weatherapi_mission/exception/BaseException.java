package com.cheolhyeon.weatherapi_mission.exception;

import com.cheolhyeon.weatherapi_mission.type.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
