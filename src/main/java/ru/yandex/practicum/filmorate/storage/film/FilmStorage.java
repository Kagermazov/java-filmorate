package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film newFilm);

    void updateFilm(Film updatedFilm);

    void deleteFilms();

    Film getFilmById(long filmId);

    List<Film> findAllFilms();
}
