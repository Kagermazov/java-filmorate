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
    @NotBlank
    @NotNull
    private final String name;
    @NotBlank
    @NotNull
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @NotNull
    private final Integer duration;
}
