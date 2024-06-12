package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    @Nullable
    @Positive
    private Long id;

    @NotBlank(message = "A film name in mandatory")
    private final String name;

    @Size(max = 200, message = "A description length is more than 200 symbols")
    @NotBlank(message = "A film description in mandatory")
    private final String description;

    @NonNull
    private final LocalDate releaseDate;

    @NonNull
    @Positive
    private final Integer duration;

    @Nullable
    private Set<Long> usersLikes;
}
