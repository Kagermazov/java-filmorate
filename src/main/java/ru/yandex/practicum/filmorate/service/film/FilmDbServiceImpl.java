package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service("filmDbServiceImpl")
public class FilmDbServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
//    private final UserStorage userStorage;

    @Autowired
    public FilmDbServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                             UserStorage userStorage) {
        this.filmStorage = filmStorage;
//        this.userStorage = userStorage;
    }

    @Override
    public FilmCreateDto addFilm(@NonNull Film newFilm) {
        return FilmMapper.mapToFilmDto(this.filmStorage.addFilm(newFilm));
    }

    @Override
    public FilmCreateDto updateFilm(@NonNull Film updatedFilm) {
//        this.filmStorage.updateFilm(updatedFilm);
//
//        Long updatedFilmId = updatedFilm.getId();
//
//        log.info("The film with an id {} was updated", updatedFilmId);
//        return this.filmStorage.getFilmById(Optional.ofNullable(updatedFilmId)
//                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        "The film " + updatedFilm.getFilmName() + " doesn't have an ID")));
        return null;
    }

    @Override
    public List<FilmCreateDto> getAllFilms() {
        log.info("The film list was created");
        return this.filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmCreateDto addLike(long userId, long filmId) {
//        Film expectedFilm = getOrThrow(filmId);
//
//        checkIfUserExist(userId);
//
//        if (expectedFilm.getUsersLikes() == null) {
//            expectedFilm.setUsersLikes(new HashSet<>());
//        }
//
//        if (expectedFilm.getUsersLikes().contains(userId)) {
//            throw new ValidationException(HttpStatus.BAD_REQUEST,
//                    "The film with an id " + filmId + " has a like from the user with an id " + userId);
//        }
//
//        expectedFilm.getUsersLikes().add(userId);
//        this.filmStorage.updateFilm(expectedFilm);
//        return expectedFilm;
        return null;
    }

    @Override
    public FilmCreateDto removeLike(long filmId, long userId) {
//        Film expectedFilm = getOrThrow(filmId);
//
//        checkIfUserExist(userId);
//        Optional.ofNullable(expectedFilm.getUsersLikes())
//                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
//                        "The film with id " + filmId + " doesn't have likes"))
//                .remove(userId);
//        return expectedFilm;
        return null;
    }

//    private void checkIfUserExist(long userId) {
//        if (this.userStorage.getUserById(userId) == null) {
//            throw new ValidationException(HttpStatus.NOT_FOUND,
//                    "The user with id " + userId + " doesn't exist");
//        }
//    }

//    private Film getOrThrow(long filmId) {
//        return Optional.ofNullable(this.filmStorage.getFilmById(filmId))
//                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
//                        "The film with id " + filmId + " doesn't exist"));
//    }

    @Override
    public List<FilmCreateDto> findPopularFilms(Integer count) {
//        log.info("Popular films list is created with limit of {}", count);
//        return this.filmStorage.findAllFilms().stream()
//                .filter(film -> film.getUsersLikes() != null)
//                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
//                .limit(count)
//                .toList();
        return List.of();
    }
}
