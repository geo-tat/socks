package com.geotat.socks.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.error("Ошибка валидации сущности", e);
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ")),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error("Сущность с данным ID не найдена.", e);
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidParametersException(final NotValidParametersException e) {
        log.error("Socks not found with specified parameters", e);
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSocksNotEnoughException(final SocksNotEnoughException e) {
        log.error("Not enough socks in stock", e);
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFormatException(final InvalidFormatException e) {
        log.error("Invalid value for Enum class Color", e);
        return new ErrorResponse(
                e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                "Invalid value for Enum class Color: " + e.getValue(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(InvalidFormatFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFormatFileException(final InvalidFormatFileException e) {
        log.error("Invalid value for Enum class Color", e);
        return new ErrorResponse(
                e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getMessage(),
                LocalDateTime.now()
        );
    }
}
