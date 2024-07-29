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

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<FilmRowDto> implements FilmStorage {
    private static final String ADD_FILM_QUERY =
            "INSERT INTO films (film_name, rating, description, release_date, duration) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_TO_FILMS_GENRE_QUERY = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String ADD_LIKE_QUERY = "INSERT INTO films_users (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM films_users WHERE film_id = ? AND user_id = ?;";
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
    public static final int BATCH_SIZE = 100;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<FilmRowDto> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public long addFilm(Film newFilm) {
        Mpa filmMpa = newFilm.getMpa();
        Long mpaId = null;

        if (filmMpa != null) {
            mpaId = filmMpa.getId();
        }

        long filmId = getFilmId(newFilm, mpaId);

        addGenresToDb(newFilm, filmId);
        addLikesToDb(newFilm, filmId);

        return filmId;
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        Mpa filmMpa = updatedFilm.getMpa();
        Long mpaId = null;

        if (filmMpa != null) {
            mpaId = filmMpa.getId();
        }

        updateFilmInfo(updatedFilm, mpaId);
    }

    @Override
    public Film getFilmById(Long id) {
        List<FilmRowDto> dtos = findMany(GET_FILM_BY_ID_QUERY, id);

        if (dtos.isEmpty()) {
            return null;
        }

        FilmRowDto firstDto = dtos.getFirst();
        Film expectedFilm = buildFilm(firstDto);

        List<Genre> genres = getGenres(dtos);

        if (!genres.contains(null)) {
            expectedFilm.setGenres(genres);
        }

        Set<Long> likes = getLikes(dtos);

        if (!likes.contains(null) && !likes.contains(0L)) {
            expectedFilm.setUsersLikes(likes);
        }

        return expectedFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public List<Film> getAllFilms() {
        List<FilmRowDto> films = findMany(GET_ALL_FILMS_QUERY);
        Map<Long, List<FilmRowDto>> filmIdToFilmRowDto = films.stream()
                .collect(groupingBy(FilmRowDto::getId));

        return getFilms(filmIdToFilmRowDto);
    }

    private List<Film> getFilms(Map<Long, List<FilmRowDto>> filmIdToFilmRowDto) {
        return filmIdToFilmRowDto.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private Film combineRows(List<FilmRowDto> dtos) {
        FilmRowDto firstDto = dtos.getFirst();
        Film filmToReturn = buildFilm(firstDto);

        Mpa filmMpa = firstDto.getMpa();

        if (filmMpa != null) {
            long rating = firstDto.getMpa().getId();
            String mpaName = firstDto.getMpa().getName();

            if (rating != 0 && mpaName != null) {
                filmMpa.setId(rating);
                filmMpa.setName(mpaName);
            }
        }

        filmToReturn.setMpa(filmMpa);

        filmToReturn.setGenres(
                getGenres(dtos)
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

    private long getFilmId(Film newFilm, Long mpaId) {
        return insert(ADD_FILM_QUERY,
                newFilm.getName(),
                mpaId,
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration());
    }

    private void addLikesToDb(Film newFilm, long filmId) {
        Optional.ofNullable(newFilm.getUsersLikes())
                .ifPresent(likes ->
                        jdbc.batchUpdate(ADD_LIKE_QUERY, likes, BATCH_SIZE,
                                (PreparedStatement ps, Long userId) -> {
                                    ps.setLong(1, filmId);
                                    ps.setLong(2, userId);
                                }));
    }

    private void addGenresToDb(Film newFilm, long filmId) {
        Optional.ofNullable(newFilm.getGenres())
                .ifPresent(genres -> {
                    Set<Long> genreIds = new HashSet<>();

                    for (Genre filmGenre : genres) {
                        genreIds.add(filmGenre.getId());
                    }

                    jdbc.batchUpdate(ADD_TO_FILMS_GENRE_QUERY, genreIds, BATCH_SIZE,
                            (PreparedStatement ps, Long genreId) -> {
                                ps.setLong(1, filmId);
                                ps.setLong(2, genreId);
                            });
                });
    }

    private void updateFilmInfo(Film updatedFilm, Long mpaId) {
        update(UPDATE_FILM_QUERY,
                updatedFilm.getName(),
                mpaId,
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getId());
    }

    private static Film buildFilm(FilmRowDto firstDto) {
        return Film.builder()
                .id(firstDto.getId())
                .name(firstDto.getName())
                .mpa(firstDto.getMpa())
                .description(firstDto.getDescription())
                .releaseDate(firstDto.getReleaseDate().toLocalDate())
                .duration(firstDto.getDuration())
                .build();
    }

    private static Set<Long> getLikes(List<FilmRowDto> dtos) {
        return dtos.stream()
                .map(FilmRowDto::getUserId)
                .collect(Collectors.toSet());
    }

    private static List<Genre> getGenres(List<FilmRowDto> dtos) {
        return dtos.stream()
                .map(FilmRowDto::getGenre)
                .toList();
    }
}
