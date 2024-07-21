package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    long addFilm(Film newFilm);

    void updateFilm(Film updatedFilm);

    Optional<Film> getFilmById(Long id);

    List<Film> getAllFilms();

    void addLike(Long filmId, Long userId);
}
