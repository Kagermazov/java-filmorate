package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

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

    public List<Film> findAllFilms() {
        return this.films.values().stream().toList();
    }

    @Override
    public Film getFilmById(long filmId) {
        return this.films.get(filmId);
    }

    @Override
    public void deleteFilms() {
        this.films.clear();
    }

    private long getNextId() {
        long currentMaxId = this.films.values().stream()
                .mapToLong(film -> Optional.ofNullable(film.getId())
                        .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "The film " + film.getName() + " doesn't have an ID")))
                .max()
                .orElse(0);
        return currentMaxId + 1;
    }
}
