package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage,
                           UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(@NonNull Film newFilm) {
        this.filmStorage.addFilm(newFilm);

        Long newFilmId = newFilm.getId();

        log.info("The film with an id {} was created", newFilmId);
        return this.filmStorage.getFilmById(Optional.ofNullable(newFilmId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The film " + newFilm.getFilmName() + " doesn't have an ID")));
    }

    @Override
    public Film updateFilm(@NonNull Film updatedFilm) {
        this.filmStorage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);
        return this.filmStorage.getFilmById(Optional.ofNullable(updatedFilmId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The film " + updatedFilm.getFilmName() + " doesn't have an ID")));
    }

    @Override
    public List<Film> findAllFilms() {
        log.info("The film list was created");
        return this.filmStorage.findAllFilms();
    }

    @Override
    public Film addLike(long userId, long filmId) {
        Film expectedFilm = getOrThrow(filmId);

        checkIfUserExist(userId);

        if (expectedFilm.getUsersLikes() == null) {
            expectedFilm.setUsersLikes(new HashSet<>());
        }

        if (expectedFilm.getUsersLikes().contains(userId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "The film with an id " + filmId + " has a like from the user with an id " + userId);
        }

        expectedFilm.getUsersLikes().add(userId);
        this.filmStorage.updateFilm(expectedFilm);
        return expectedFilm;
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        Film expectedFilm = getOrThrow(filmId);

        checkIfUserExist(userId);
        Optional.ofNullable(expectedFilm.getUsersLikes())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "The film with id " + filmId + " doesn't have likes"))
                .remove(userId);
        return expectedFilm;
    }

    private void checkIfUserExist(long userId) {
        if (this.userStorage.getUserById(userId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "The user with id " + userId + " doesn't exist");
        }
    }

    private Film getOrThrow(long filmId) {
        return Optional.ofNullable(this.filmStorage.getFilmById(filmId))
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "The film with id " + filmId + " doesn't exist"));
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        log.info("Popular films list is created with limit of {}", count);
        return this.filmStorage.findAllFilms().stream()
                .filter(film -> film.getUsersLikes() != null)
                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                .limit(count)
                .toList();
    }

    @Override
    public Film getFilmById(long filmId) {
        return this.filmStorage.getFilmById(filmId);
    }
}
