package ru.yandex.practicum.filmorate.dto.film;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.film.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    @Nullable
    @Positive
    private Long id;

    @NotBlank(message = "A film name in mandatory")
    private String name;

    @Nullable
    private MpaDto mpa;

    @Size(max = 200, message = "A description length is more than 200 symbols")
    @NotBlank(message = "A film description in mandatory")
    private String description;

    @NonNull
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private Integer duration;

    @Nullable
    private List<GenreDto> genres;

    @Nullable
    private Set<Long> usersLikes;
}
