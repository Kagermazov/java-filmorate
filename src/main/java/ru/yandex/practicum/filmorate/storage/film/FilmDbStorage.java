package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
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
            "genre.genre_name AS genre_id, " +
            "mpa.id AS mpa_id, " +
            "mpa.mpa_name, " +
            "fu.user_id " +
            "FROM films " +
            "LEFT JOIN films_genre ON films.id=films_genre.film_id " +
            "LEFT JOIN genre ON films_genre.genre_id=genre.id " +
            "LEFT join films_mpa ON films.id=films_mpa.film_id " +
            "LEFT JOIN mpa ON films_mpa.mpa_id=mpa.id " +
            "LEFT JOIN films_users fu ON films.id =fu.film_id;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public long addFilm(Film newFilm) {

        long id = insert(ADD_FILM_QUERY,
                newFilm.getName(),
                newFilm.getMpa().getId(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration());

        Optional.ofNullable(newFilm.getGenres())
                .ifPresent(genres -> {
                    for (Genre filmGenre : genres) {
                        insert(ADD_TO_FILMS_GENRE_QUERY, newFilm.getId(), filmGenre.getId());
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
        return jdbc.query(GET_ALL_FILMS_QUERY, mapper);
    }
}
