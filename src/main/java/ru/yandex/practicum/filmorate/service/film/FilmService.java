package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film newFilm);

    Film updateFilm(Film updatedFilm);

    List<Film> findAllFilms();

    Film getFilmById(long filmId);

    Film addLike(long userId, long filmId);

    Film removeLike(long filmId, long userId);

    List<Film> findPopularFilms(Integer count);
}
