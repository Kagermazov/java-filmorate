package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    public static final String ADD_FILM =
            "INSERT INTO films (film_name, rating, description, release_date, duration) VALUES (?, ?, ?, ?, ?);";
    public static final String ADD_TO_FILMS_GENRE = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?);";
    public static final String GET_ALL = "SELECT films.*, " +
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
    public Film addFilm(Film newFilm) {
        long id = insert(ADD_FILM,
                newFilm.getName(),
                newFilm.getMpa().getId(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration());

        newFilm.setId(id);

        Optional<Set<Genre>> optionalGenres = Optional.ofNullable(newFilm.getGenres());

        if (optionalGenres.isPresent()) {
            Set<Genre> genres = optionalGenres.get();

            for (Genre genre_id : genres) {
                insert(ADD_TO_FILMS_GENRE, newFilm.getId(), genre_id.getId());
            }
        }

        return newFilm;
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        update(ADD_FILM,
                updatedFilm.getName(),
                updatedFilm.getMpa(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration());;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbc.query(GET_ALL, mapper);
    }
}
