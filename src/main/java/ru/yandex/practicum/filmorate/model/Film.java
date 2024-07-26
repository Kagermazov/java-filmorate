package ru.yandex.practicum.filmorate.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.service.film.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Builder
public class Film {
    @Nullable
    @Positive
    private final Long id;

    @NotBlank(message = "A film name in mandatory")
    private final String name;

    @Nullable
    private final Mpa mpa;

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
    private List<Genre> genres;

    @Nullable
    private Set<Long> usersLikes;
}
