package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    FilmDto addFilm(Film newFilm);

    FilmDto updateFilm(Film updatedFilm);

    List<FilmDto> getAllFilms();

    FilmDto addLike(long userId, long filmId);

    FilmDto removeLike(long filmId, long userId);

    List<FilmDto> findPopularFilms(Integer count);
}
