package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service("filmDbServiceImpl")
public class FilmDbServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmDbServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                             @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public FilmDto addFilm(@NonNull Film newFilm) {

        validateMPA(newFilm.getMpa());
        validateGenres(newFilm.getGenres());
        newFilm.setId(this.filmStorage.addFilm(newFilm));
        return FilmMapper.mapToFilmDto(newFilm);
    }

    private void validateGenres(List<Genre> genres) {
        Optional.ofNullable(genres)
                .ifPresent(filmGenres -> {
                    for (Genre filmGenre : filmGenres) {
                        Long idOfGenre = filmGenre.getId();
                        if (idOfGenre == null || isGenrePresent(idOfGenre)) {
                            throw new ValidationException(HttpStatus.BAD_REQUEST, "Fail genre");
                        }
                    }
                });
    }

//    todo replace with sql query
    private static boolean isGenrePresent(Long idOfGenre) {
        return idOfGenre > 6;
    }

    private void validateMPA(FilmRating mpa) {
        try {
            Long mpaId = mpa.getId();

            if (mpaId == null || isMpaPresent(mpaId)) {
                throw new ValidationException(HttpStatus.BAD_REQUEST, "Fail MPA");
            }
        } catch (NullPointerException e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Fail MPA");
        }
    }

//    todo replace with sql query
    private static boolean isMpaPresent(Long mpaId) {
        return mpaId > 5;
    }

    @Override
    public FilmDto updateFilm(@NonNull Film updatedFilm) {
        validateMPA(updatedFilm.getMpa());
        validateGenres(updatedFilm.getGenres());
        this.filmStorage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);

        return this.filmStorage.getAllFilms().stream()
                .filter(film -> film.getId().equals(updatedFilmId))
                .findFirst()
                .map(FilmMapper::mapToFilmDto)
                .get();
    }

    @Override
    public List<FilmDto> getAllFilms() {
        log.info("The film list was created");

        return this.filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto addLike(long userId, long filmId) {
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
    public FilmDto removeLike(long filmId, long userId) {
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
    public List<FilmDto> findPopularFilms(Integer count) {
//        log.info("Popular films list is created with limit of {}", count);
//        return this.filmStorage.findAllFilms().stream()
//                .filter(film -> film.getUsersLikes() != null)
//                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
//                .limit(count)
//                .toList();
        return List.of();
    }
}
