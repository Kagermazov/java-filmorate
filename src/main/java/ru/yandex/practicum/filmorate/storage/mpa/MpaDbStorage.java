package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.mpa.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    private static final String FIND_MPA_NAME_QUERY = "SELECT * " +
            "FROM MPA " +
            "WHERE mpa.id = ?;";

    private static final String FIND_ALL_QUERY = "SELECT * " +
            "FROM MPA;";

    public Optional<Mpa> findMpaName(long id) {
        try {
            Mpa result = this.jdbc.queryForObject(FIND_MPA_NAME_QUERY, mapper, id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Mpa> findAllMpa() {
        return this.jdbc.query(FIND_ALL_QUERY, mapper);
    }
}
