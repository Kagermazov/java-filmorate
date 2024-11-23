package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FilmRepositoryTest {

    @Autowired
    private FilmRepository repository;

    @Test
    void givenFilm_whenSaveFilm_thenReturnFilm(){

        Film testFilm = Film.builder().
                id(1L).
                name("Name").
                description("Description").
                releaseDate(LocalDate.EPOCH).
                duration(1).
                build();

        Film savedFilm = repository.save(testFilm);

        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isGreaterThan(0);
    }
}