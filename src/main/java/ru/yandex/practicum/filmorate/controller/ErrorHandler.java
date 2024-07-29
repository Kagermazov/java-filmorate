package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse response = new ErrorResponse(e.toString(), e.getMessage());

        log.warn("Exception: ", e);
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
        ErrorResponse response = new ErrorResponse(e.toString(), e.getMessage());

        log.warn("Exception: ", e);
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Exception: ", e);
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        log.warn("One of a controller method parameters is wrong", e);
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(RuntimeException e) {
        log.error("The internal server error", e);
        return new ErrorResponse(e.toString(), e.getMessage());
    }
}
