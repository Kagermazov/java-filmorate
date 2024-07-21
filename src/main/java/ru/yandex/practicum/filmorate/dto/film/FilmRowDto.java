package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;

@Data
@NoArgsConstructor
public class FilmRowDto {
    Long id;
    String name;
    String description;
    Date releaseDate;
    Integer duration;
    Mpa mpa;
    Genre genre;
    Long userId;
}