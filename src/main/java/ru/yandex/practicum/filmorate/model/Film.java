package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    @Nullable
    private Long id;

    @NotBlank(message = "A film name in mandatory")
    private final String name;

    @NotBlank(message = "A film description in mandatory")
    private final String description;

    @NonNull
    private final LocalDate releaseDate;

    @NonNull
    private final Integer duration;

    @Nullable
    private Set<Long> usersLikes;
}
