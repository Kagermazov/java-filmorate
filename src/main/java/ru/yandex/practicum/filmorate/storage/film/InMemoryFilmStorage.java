package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryFilmStorage")
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long counter;

    @Override
    public long addFilm(@NonNull Film newFilm) {
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm.getId();
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        Long updatedFilmId = updatedFilm.getId();

        if (!films.containsKey(updatedFilmId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no film with id " + updatedFilmId);
        }

        films.replace(updatedFilmId, updatedFilm);
    }

    public void deleteFilms() {
        films.clear();
    }

    @Override
    public List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        return List.of();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    private long getNextId() {
        return ++counter;
    }
}
