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

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long counter;

    @Override
    public void addFilm(@NonNull Film newFilm) {
        newFilm.setId(getNextId());
        this.films.put(newFilm.getId(), newFilm);
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        Long updatedFilmId = updatedFilm.getId();

        if (!this.films.containsKey(updatedFilmId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no film with id " + updatedFilmId);
        }

        this.films.replace(updatedFilmId, updatedFilm);
    }

    @Override
    public void deleteFilms() {
        this.films.clear();
    }

    @Override
    public List<Film> findAllFilms() {
        return this.films.values().stream().toList();
    }

    @Override
    public Film getFilmById(long filmId) {
        return this.films.get(filmId);
    }

    private long getNextId() {
        return ++this.counter;
    }
}
