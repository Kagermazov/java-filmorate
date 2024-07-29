package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier("inMemoryFilmStorage") FilmStorage storageForFilms,
                           @Qualifier("inMemoryUserStorage") UserStorage storageForUsers) {
        filmStorage = storageForFilms;
        userStorage = storageForUsers;
    }

    @Override
    public FilmDto addFilm(@NonNull Film newFilm) {
        filmStorage.addFilm(newFilm);

        Long newFilmId = newFilm.getId();

        log.info("The film with an id {} was created", newFilmId);
        return FilmMapper.mapToFilmDto(newFilm);

    }

    @Override
    public FilmDto updateFilm(@NonNull Film updatedFilm) {
        filmStorage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    @Override
    public List<FilmDto> getAllFilms() {
        log.info("The film list was created");
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto getFilmById(Long id) {
        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(id));
    }

    public void addLike(Long userId, Long filmId) {
        Film expectedFilm = filmStorage.getAllFilms().stream()
                .filter(film -> Objects.equals(film.getId(), filmId))
                .findAny()
                .orElse(null);

        checkIfUserExist(userId);

        if (expectedFilm.getUsersLikes() == null) {
            expectedFilm.setUsersLikes(new HashSet<>());
        }

        if (expectedFilm.getUsersLikes().contains(userId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "The film with an id " + filmId + " has a like from the user with an id " + userId);
        }

        expectedFilm.getUsersLikes().add(userId);
        filmStorage.updateFilm(expectedFilm);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film expectedFilm = filmStorage.getAllFilms().stream()
                .filter(film -> film.getId() == filmId)
                .findAny()
                .orElse(null);

        checkIfUserExist(userId);
        Optional.ofNullable(expectedFilm.getUsersLikes())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "The film with id " + filmId + " doesn't have likes"))
                .remove(userId);
    }

    private void checkIfUserExist(Long userId) {
        userStorage.findAllUsers().stream()
                .filter(user -> Objects.equals(user.getId(), userId))
                .findFirst().orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "The user with id " + userId + " doesn't exist"));
    }

    @Override
    public List<FilmDto> findPopularFilms(Integer count) {
        log.info("Popular films list is created with limit of {}", count);
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getUsersLikes() != null)
                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
