package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.film.FilmRowDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<FilmRowDto> implements FilmStorage {
    private static final String ADD_FILM_QUERY =
            "INSERT INTO films (film_name, rating, description, release_date, duration) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_TO_FILMS_GENRE_QUERY = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String ADD_LIKE_QUERY = "INSERT INTO films_users (film_id, user_id) VALUES (?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET film_name = ?, " +
            "rating = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ? " +
            "WHERE id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT films.*, " +
            "mpa.mpa_name, " +
            "genre.id as genre_id, " +
            "genre_name, " +
            "fu.user_id " +
            "FROM films " +
            "LEFT JOIN films_genre ON films.id=films_genre.film_id " +
            "LEFT JOIN mpa ON films.rating = mpa.id " +
            "LEFT JOIN genre ON films_genre.genre_id=genre.id " +
            "LEFT JOIN films_users fu ON films.id =fu.film_id";
    private static final String GET_FILM_BY_ID_QUERY = "SELECT films.*, " +
            "mpa.mpa_name, " +
            "genre.id as genre_id, " +
            "genre_name, " +
            "fu.user_id " +
            "FROM films " +
            "LEFT JOIN films_genre ON films.id = films_genre.film_id " +
            "LEFT JOIN mpa on films.rating = mpa.id " +
            "LEFT JOIN genre ON films_genre.genre_id = genre.id " +
            "LEFT JOIN films_users fu ON films.id = fu.film_id " +
            "WHERE films.id = ?;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<FilmRowDto> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public long addFilm(Film newFilm) {

        Long mpaId = newFilm.getMpa().getId();

        long id = insert(ADD_FILM_QUERY,
                newFilm.getName(),
                mpaId,
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration());

        Optional.ofNullable(newFilm.getGenres())
                .ifPresent(genres -> {
                    Set<Long> genreIds = new HashSet<>();

                    for (Genre filmGenre : genres) {
                        genreIds.add(filmGenre.getId());
                    }

                    for (Long genreId : genreIds) {
                        insert(ADD_TO_FILMS_GENRE_QUERY, id, genreId);
                    }
                });

        return id;
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        update(UPDATE_FILM_QUERY,
                updatedFilm.getName(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        List<FilmRowDto> dtos = findMany(GET_FILM_BY_ID_QUERY, id);

        if (dtos.isEmpty()) {
            return null;
        }

        Film expectedFilm = new Film();
        FilmRowDto firstDto = dtos.getFirst();

        expectedFilm.setId(firstDto.getId());
        expectedFilm.setName(firstDto.getName());
        expectedFilm.setMpa(firstDto.getMpa());
        expectedFilm.setDescription(firstDto.getDescription());
        expectedFilm.setReleaseDate(firstDto.getReleaseDate().toLocalDate());
        expectedFilm.setDuration(firstDto.getDuration());

        List<Genre> genres = dtos.stream()
                .map(FilmRowDto::getGenre)
                .toList();

        if (!genres.contains(null)) {
            expectedFilm.setGenres(genres);
        }

        return expectedFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Film> getAllFilms() {
        List<FilmRowDto> films = findMany(GET_ALL_FILMS_QUERY);
        Map<Long, List<FilmRowDto>> filmIdToFilmRowDto = films.stream()
                .collect(groupingBy(FilmRowDto::getId));

        return filmIdToFilmRowDto.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private Film combineRows(List<FilmRowDto> dtos) {
        Film filmToReturn = new Film();
        FilmRowDto firstDto = dtos.getFirst();

        filmToReturn.setId(firstDto.getId());
        filmToReturn.setName(firstDto.getName());

        Mpa filmMpa = new Mpa();

        filmMpa.setId(firstDto.getMpa().getId());
        filmMpa.setName(firstDto.getMpa().getName());

        filmToReturn.setMpa(filmMpa);
        filmToReturn.setDescription(firstDto.getDescription());
        filmToReturn.setDuration(firstDto.getDuration());
        filmToReturn.setReleaseDate(firstDto.getReleaseDate().toLocalDate());

        filmToReturn.setGenres(
                dtos.stream()
                        .map(FilmRowDto::getGenre)
                        .toList()
        );

        filmToReturn.setUsersLikes(
                new LinkedHashSet<>(
                        dtos.stream()
                                .map(FilmRowDto::getUserId)
                                .distinct()
                                .sorted()
                                .toList()
                )
        );

        return filmToReturn;
    }
}
