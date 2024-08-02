package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
//@RequiredArgsConstructor
public class MpaDbStorage extends BaseRepository<Mpa> {
    private static final String FIND_MPA_NAME_QUERY = "SELECT * " +
            "FROM MPA " +
            "WHERE mpa.id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * " +
            "FROM MPA;";
    private static final String COUNT_MPAS_QUERY = "SELECT COUNT(id) FROM MPA;";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findMpaName(long id) {
        return findOne(FIND_MPA_NAME_QUERY, id);
    }

    public List<Mpa> findAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }

    public Long countMpas() {
        return countRows(COUNT_MPAS_QUERY);
    }
}
