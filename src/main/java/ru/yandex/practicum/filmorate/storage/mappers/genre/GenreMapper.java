package ru.yandex.practicum.filmorate.storage.mappers.genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre expectedGenre = new Genre();

        expectedGenre.setId(rs.getLong("id"));
        expectedGenre.setName(rs.getString("genre_name"));
        return expectedGenre;
    }
}
