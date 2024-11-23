package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    public static final String FILM_NOT_FOUND = "There's now film with an id";
    private final FilmRepository repository;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Autowired
    public FilmService(FilmRepository storageForFilms,
                       GenreDbStorage storageForGenres,
                       MpaDbStorage storageForMpa) {
        repository = storageForFilms;
        genreStorage = storageForGenres;
        mpaStorage = storageForMpa;
    }

    public FilmDto addFilm(@NonNull Film newFilm) {
        validateMPA(newFilm.getMpa());
        validateGenres(newFilm.getGenres());

        Long filmId = repository.save(newFilm).getId();

        Optional<Film> optionalFilm = repository.findById(filmId);

        checkIfFilmExits(optionalFilm, HttpStatus.INTERNAL_SERVER_ERROR, "The film wasn't saved");

        return FilmMapper.mapToFilmDto(optionalFilm.get());
    }

    public FilmDto updateFilm(@NonNull Film updatedFilm) {
        validateMPA(updatedFilm.getMpa());
        validateGenres(updatedFilm.getGenres());
        repository.save(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);
        return getFilmDto(updatedFilmId);
    }

    public List<FilmDto> getAllFilms() {
        log.info("The film list was created");
        return getFilmDtos();
    }

    public FilmDto getFilmById(Long id) {
        Optional<Film> optionalFilm = repository.findById(id);

        checkIfFilmExits(optionalFilm, HttpStatus.NOT_FOUND, FILM_NOT_FOUND + id);

        return FilmMapper.mapToFilmDto(optionalFilm.get());
    }

    public void addLike(Long filmId, Long userId) {
        Optional<Film> optionalFilm = repository.findById(filmId);

        checkIfFilmExits(optionalFilm, HttpStatus.NOT_FOUND, FILM_NOT_FOUND + filmId);

        Film updatedFilm = optionalFilm.get();

        optionalFilm.get().getUsersLikes().add(userId);
        repository.save(updatedFilm);
    }

    public void removeLike(Long filmId, Long userId) {
        Optional<Film> optionalFilm = repository.findById(filmId);

        checkIfFilmExits(optionalFilm, HttpStatus.NOT_FOUND, FILM_NOT_FOUND + filmId);

        Film updatedFilm = optionalFilm.get();

        optionalFilm.get().getUsersLikes().remove(userId);
        repository.save(updatedFilm);
    }

    public List<FilmDto> findPopularFilms(Integer limit) {
        log.info("Popular films list is created with limit of {}", limit);
        return getPopularFilms(limit);
    }

    //todo to write getPopularFilms method
    private List<FilmDto> getPopularFilms(Integer limit) {
        return null;
//                repository.findAll().stream()
//                .map(FilmMapper::mapToFilmDto)
//                .toList();
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
        if (mpa == null) {
            return;
        }

        Long mpaId = mpa.getId();

        if (mpaId == null || isMpaPresent(mpaId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Fail MPA");
        }
    }

    private boolean isMpaPresent(Long mpaId) {
        return mpaId > mpaStorage.countMpas();
    }

    private FilmDto getFilmDto(Long updatedFilmId) {
        Optional<Film> optionalFilm = repository.findById(updatedFilmId);

        checkIfFilmExits(optionalFilm, HttpStatus.NOT_FOUND, FILM_NOT_FOUND + updatedFilmId);

        return FilmMapper.mapToFilmDto(optionalFilm.get());
    }

    private static void checkIfFilmExits(Optional<Film> optionalFilm, HttpStatus notFound, String filmIdNotFound) {
        if (optionalFilm.isEmpty()) {
            throw new EntityNotFoundException(notFound, filmIdNotFound);
        }
    }

    private List<FilmDto> getFilmDtos() {
        return repository.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
