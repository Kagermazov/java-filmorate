package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> {
    public static final String FIND_GENRE_BY_ID_QUERY = "SELECT * " +
            "FROM GENRE " +
            "WHERE id = ?;";

    public static final String FIND_ALL_GENRES = "SELECT * " +
            "FROM GENRE;";
    private static final String COUNT_GENRES_QUERY = "SELECT COUNT(id) FROM GENRE;";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> findGenreById(Long id) {
        return findOne(FIND_GENRE_BY_ID_QUERY, id);
    }

    public List<Genre> findAllGenres() {
        return findMany(FIND_ALL_GENRES);
    }

    public Long countGenres() {
        return countRows(COUNT_GENRES_QUERY);
    }
}
