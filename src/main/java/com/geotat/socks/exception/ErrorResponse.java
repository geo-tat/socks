package com.geotat.socks.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ошибка ответа")
public class ErrorResponse {

    @Schema(description = "Тип исключения")
    private String exception;

    @Schema(description = "Класс-источник исключения")
    private String sourceClass;

    @Schema(description = "Сообщение об ошибке")
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Метка времени ошибки")
    private LocalDateTime timestamp;
}