package ru.yandex.practicum.filmorate.service.film;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    @Getter
    private final FilmStorage storage;
    private final int maxStreamSize;
    private final int maxDescriptionLength;
    private final String cinemaInventionDate;

    @Autowired
    public FilmServiceImpl(FilmStorage storage,
                           @Value("${filmorate.MAX_STREAM_SIZE}") int streamSize,
                           @Value("${filmorate.MAX_DESCRIPTION_LENGTH}") int maxDescriptionLength,
                           @Value("${filmorate.CINEMA_INVENTION_DATE}") String cinemaInventionDate) {
        this.storage = storage;
        this.maxStreamSize = streamSize;
        this.maxDescriptionLength = maxDescriptionLength;
        this.cinemaInventionDate = cinemaInventionDate;
    }

    @Override
    public Film addFilm(@NonNull Film newFilm) {
        validate(newFilm);
        this.storage.addFilm(newFilm);

        Long newFilmId = newFilm.getId();

        log.info("The film with an id {} was created", newFilmId);
        return this.storage.getFilmById(Optional.ofNullable(newFilmId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The film " + newFilm.getName() + " doesn't have an ID")));
    }

    @Override
    public Film updateFilm(@NonNull Film updatedFilm) {
        validate(updatedFilm);
        this.storage.updateFilm(updatedFilm);

        Long updatedFilmId = updatedFilm.getId();

        log.info("The film with an id {} was updated", updatedFilmId);
        return this.storage.getFilmById(Optional.ofNullable(updatedFilmId)
                .orElseThrow(() -> new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "The film " + updatedFilm.getName() + " doesn't have an ID")));
    }

    @Override
    public List<Film> findAllFilms() {
        log.info("The film list was created");
        return this.storage.findAllFilms();
    }

    @Override
    public Film addLike(long userId, long filmId) {
        checkIfNegative(filmId);
        checkIfNegative(userId);

        Film expectedFilm = this.storage.getFilmById(filmId);

        if (expectedFilm.getUsersLikes() == null) {
            expectedFilm.setUsersLikes(new HashSet<>());
            expectedFilm.getUsersLikes().add(userId);
            return expectedFilm;
        }

        if (expectedFilm.getUsersLikes().contains(userId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "The film with an id " + filmId + " has a like from the user with an id " + userId);
        }

        expectedFilm.getUsersLikes().add(userId);
        this.storage.updateFilm(expectedFilm);
        return expectedFilm;
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        checkIfNegative(filmId);
        checkIfNegative(userId);

        Film expectedFilm = getFilmById(filmId);

        Optional.ofNullable(expectedFilm.getUsersLikes())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "The film with id " + filmId + " doesn't have likes"))
                .remove(userId);
        return expectedFilm;
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        if (count == null) {

            log.info("Popular films list is created");
            return this.storage.findAllFilms().stream()
                    .filter(film -> film.getUsersLikes() != null)
                    .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                    .limit(maxStreamSize)
                    .toList();
        }

        log.info("Popular films list is created with limit of {}", count);
        return this.storage.findAllFilms().stream()
                .filter(film -> film.getUsersLikes() != null)
                .sorted(Comparator.comparing(film -> -1 /*reversed*/ * film.getUsersLikes().size()))
                .limit(count)
                .toList();
    }

    @Override
    public Film getFilmById(long filmId) {
        checkIfNegative(filmId);

        if (filmId < 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The id is negative");
        }

        return this.storage.getFilmById(filmId);
    }

    private void validate(Film filmToCheck) {
        if (filmToCheck.getDescription().length() > this.maxDescriptionLength) {
            log.warn("A description length is more than 200 symbols");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "A description length is more than 200 symbols");
        }

        if (filmToCheck.getReleaseDate().isBefore(LocalDate.parse(this.cinemaInventionDate))) {
            log.warn("The release date is wrong");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Cinema didn't exist that time");
        }

        if (filmToCheck.getDuration() < 0) {
            log.warn("The duration is negative");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Duration can`t be negative");
        }
    }

    private void checkIfNegative(long number) {
        if (number < 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "One of ids is negative");
        }
    }
}
