package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.service.film.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @Nullable
    @Positive
    private Long id;

    @NotBlank(message = "A film name in mandatory")
    private final String name;

    @Nullable
    private final List<String> genre;

    @Nullable
    private final FilmRating rating;

    @Size(max = 200, message = "A description length is more than 200 symbols")
    @NotBlank(message = "A film description in mandatory")
    private final String description;

    @NonNull
    @ReleaseDateConstraint
    private final LocalDate releaseDate;

    @NonNull
    @Positive
    private final Integer duration;

    @Nullable
    private Set<Long> usersLikes;
}
