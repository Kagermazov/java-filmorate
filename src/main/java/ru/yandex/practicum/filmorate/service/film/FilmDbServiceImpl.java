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
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmDbServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Autowired
    public FilmDbServiceImpl(@Qualifier("filmDbStorage") FilmStorage storageForFilms,
                             GenreDbStorage storageForGenres,
                             MpaDbStorage storageForMpa) {
        filmStorage = storageForFilms;
        genreStorage = storageForGenres;
        mpaStorage = storageForMpa;
    }

    @Override
    public FilmDto addFilm(@NonNull Film newFilm) {
        validateMPA(newFilm.getMpa());
        validateGenres(newFilm.getGenres());

        Long filmId = filmStorage.addFilm(newFilm);

        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(filmId));
    }

    @Override
    public FilmDto updateFilm(@NonNull Film updatedFilm) {
        validateMPA(updatedFilm.getMpa());
        validateGenres(updatedFilm.getGenres());
        filmStorage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);

        return getFilmDto(updatedFilmId);
    }

    @Override
    public List<FilmDto> getAllFilms() {
        log.info("The film list was created");
        return getFilmDtos();
    }

    @Override
    public FilmDto getFilmById(Long id) {
        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(id));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<FilmDto> findPopularFilms(Integer count) {
        log.info("Popular films list is created with limit of {}", count);
        return getPopularFilms(count);
    }

    private List<FilmDto> getPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getUsersLikes() != null)
                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
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

    private boolean isGenrePresent(Long idOfGenre) {
        return idOfGenre > genreStorage.countGenres();
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

    private boolean isMpaPresent(Long mpaId) {
        return mpaId > mpaStorage.countMpas();
    }

    private FilmDto getFilmDto(Long updatedFilmId) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getId().equals(updatedFilmId))
                .findFirst()
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The film with an id " + updatedFilmId + " was updated, but not found in the storage"));
    }

    private List<FilmDto> getFilmDtos() {
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
