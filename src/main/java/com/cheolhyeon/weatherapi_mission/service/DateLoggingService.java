package com.cheolhyeon.weatherapi_mission.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Aspect
@Component
@Slf4j
public class DateLoggingService {
    @Around("@annotation(com.cheolhyeon.weatherapi.aop.RequestDateLogging)")
    public Object dateLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
        log.info("Request Date = {}", format);
        return joinPoint.proceed();
    }
}

