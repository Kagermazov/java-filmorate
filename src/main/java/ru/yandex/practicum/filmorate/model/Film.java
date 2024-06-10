package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film implements BaseEntity {
    private int id;
    @NotBlank(message = "A film name in mandatory")
    private final String name;
    @NotBlank(message = "A film description in mandatory")
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Integer duration;
}
