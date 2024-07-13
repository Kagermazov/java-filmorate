package ru.yandex.practicum.filmorate.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.service.film.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class FilmCreateDto {
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
    private Set<GenreDto> genres;

    @Nullable
    private Set<Long> usersLikes;
}
