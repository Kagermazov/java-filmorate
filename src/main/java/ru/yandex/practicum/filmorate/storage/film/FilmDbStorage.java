package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.mappers.film.FilmsRowMapper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<FilmsRowMapper.FilmRowDto> implements FilmStorage {
    private static final String ADD_FILM_QUERY =
            "INSERT INTO films (film_name, rating, description, release_date, duration) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_TO_FILMS_GENRE_QUERY = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET film_name = ?, " +
            "rating = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ? " +
            "WHERE id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT films.*, " +
            "films_genre.genre_id as genre_id, " +
            "fu.user_id " +
            "FROM films " +
            "LEFT JOIN films_genre ON films.id=films_genre.film_id " +
            "left JOIN films_users fu ON films.id =fu.film_id ;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<FilmsRowMapper.FilmRowDto> mapper) {
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
                    for (Genre filmGenre : genres) {
                        insert(ADD_TO_FILMS_GENRE_QUERY, id, filmGenre.getId());
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
    public List<Film> getAllFilms() {
        List<FilmsRowMapper.FilmRowDto> films = jdbc.query(GET_ALL_FILMS_QUERY, mapper);

        Map<Long, List<FilmsRowMapper.FilmRowDto>> filmIdToFilmRowDto = films.stream()
                .collect(groupingBy(FilmsRowMapper.FilmRowDto::getId));

        return filmIdToFilmRowDto.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private Film combineRows(List<FilmsRowMapper.FilmRowDto> dtos) {
        Film filmToReturn = new Film();
        FilmsRowMapper.FilmRowDto firstDto = dtos.getFirst();

        filmToReturn.setId(firstDto.getId());
        filmToReturn.setName(firstDto.getName());

        FilmRating mpa = new FilmRating();

        mpa.setId(firstDto.getMpaId());

        filmToReturn.setMpa(mpa);
        filmToReturn.setDescription(firstDto.getDescription());
        filmToReturn.setDuration(firstDto.getDuration());
        filmToReturn.setReleaseDate(firstDto.getReleaseDate().toLocalDate());

        filmToReturn.setGenres(
                dtos.stream()
                        .map(FilmsRowMapper.FilmRowDto::getGenreId)
                        .distinct()
                        .sorted()
                        .map(id -> {
                            Genre filmGenre = new Genre();
                            filmGenre.setId(id);
                            return filmGenre;
                        })
                        .toList()
        );

        filmToReturn.setUsersLikes(
                new LinkedHashSet<>(
                        dtos.stream()
                                .map(FilmsRowMapper.FilmRowDto::getUserId)
                                .distinct()
                                .sorted()
                                .toList()
                )
        );

        return filmToReturn;
    }
}
