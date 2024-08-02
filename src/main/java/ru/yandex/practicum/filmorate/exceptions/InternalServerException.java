package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends RuntimeException {
    private final HttpStatus status;

    public InternalServerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
