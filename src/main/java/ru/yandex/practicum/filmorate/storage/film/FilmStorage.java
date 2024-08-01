package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    long addFilm(Film newFilm);

    void addLike(Long filmId, Long userId);

    List<Film> getAllFilms();

    List<Film> getPopularFilms(Integer limit);

    Film getFilmById(Long id);

    void updateFilm(Film updatedFilm);

    void deleteLike(Long filmId, Long userId);
}
