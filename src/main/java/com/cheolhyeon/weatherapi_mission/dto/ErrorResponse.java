package com.cheolhyeon.weatherapi_mission.dto;

import com.cheolhyeon.weatherapi_mission.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
