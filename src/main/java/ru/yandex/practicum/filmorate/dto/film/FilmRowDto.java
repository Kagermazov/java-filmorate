package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FilmRowDto {
    private Long id;
    private String name;
    private String description;
    private Date releaseDate;
    private Integer duration;
    private Mpa mpa;
    private Genre genre;
    private Long userId;
}