package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    long addFilm(Film newFilm);

    void updateFilm(Film updatedFilm);

    List<Film> getAllFilms();

    Film getFilmById(Long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
