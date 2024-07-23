package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("filmDbServiceImpl")
public class FilmDbServiceImpl implements FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmDbServiceImpl(@Qualifier("filmDbStorage") FilmStorage storageForFilms) {
        storage = storageForFilms;
    }

    @Override
    public FilmDto addFilm(@NonNull Film newFilm) {
        validateMPA(newFilm.getMpa());
        validateGenres(newFilm.getGenres());
        Long filmId = storage.addFilm(newFilm);
        return FilmMapper.mapToFilmDto(storage.getFilmById(filmId));
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

    private void validateMPA(Mpa mpa) {
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
        storage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);

        return storage.getAllFilms().stream()
                .filter(film -> film.getId().equals(updatedFilmId))
                .findFirst()
                .map(FilmMapper::mapToFilmDto)
                .get();
    }

    @Override
    public List<FilmDto> getAllFilms() {
        log.info("The film list was created");

        return storage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto getFilmById(Long id) {
        return FilmMapper.mapToFilmDto(storage.getFilmById(id));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        storage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        storage.removeLike(filmId, userId);
    }

    @Override
    public List<FilmDto> findPopularFilms(Integer count) {
        log.info("Popular films list is created with limit of {}", count);
        return storage.getAllFilms().stream()
                .filter(film -> film.getUsersLikes() != null)
                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
