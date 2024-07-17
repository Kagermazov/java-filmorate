package ru.yandex.practicum.filmorate.storage.mappers.mpa;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa expectedMpa = new Mpa();

        expectedMpa.setId(rs.getLong("id"));
        expectedMpa.setName(rs.getString("mpa_name"));
        return expectedMpa;
    }
}
