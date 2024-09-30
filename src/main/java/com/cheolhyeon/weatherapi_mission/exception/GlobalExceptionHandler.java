package com.cheolhyeon.weatherapi_mission.exception;

import com.cheolhyeon.weatherapi_mission.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private void logException(BaseException e) {
        log.error("{} is occurred {}", e.getErrorCode(), e.getErrorMessage());
    }
    @ExceptionHandler(DiaryException.class)
    public ErrorResponse handleDiaryException(DiaryException e) {
        logException(e);
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }


    @ExceptionHandler(JsonParserException.class)
    public ErrorResponse handleJsonParserException(JsonParserException e) {
        logException(e);
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }


    @ExceptionHandler(URLException.class)
    public ErrorResponse handleURLException(URLException e) {
        logException(e);
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }
}
