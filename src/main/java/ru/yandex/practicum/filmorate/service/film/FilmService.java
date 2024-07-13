package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    FilmCreateDto addFilm(Film newFilm);

    FilmCreateDto updateFilm(Film updatedFilm);

    List<FilmCreateDto> getAllFilms();

    FilmCreateDto addLike(long userId, long filmId);

    FilmCreateDto removeLike(long filmId, long userId);

    List<FilmCreateDto> findPopularFilms(Integer count);
}
