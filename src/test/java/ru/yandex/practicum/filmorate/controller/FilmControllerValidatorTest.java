package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerValidatorTest {

    FilmController controller;
            
    @BeforeEach
    public void beforeEach() {
        this.controller = new FilmController();
    }

    @DisplayName("Shouldn't throw an exception if an object is correct")
    @Test
    void shouldNotThrowExceptionIfObjectIsCorrect() {
        Film testFilm = new Film("Name", "Description", LocalDate.EPOCH, 1);

        assertDoesNotThrow(() -> this.controller.validate(testFilm));
    }

    @DisplayName("Should throw an exception if an object name is empty or blank")
    @Test
    void shouldThrowExceptionIfObjectNameIsEmptyOrBlank() {
        Film testFilm = new Film("", "Description", LocalDate.EPOCH, 1);
        Film testFilm2 = new Film(" ", "Description", LocalDate.EPOCH, 1);

        assertThrows(ValidationException.class, () -> this.controller.validate(testFilm));
        assertThrows(ValidationException.class, () -> this.controller.validate(testFilm2));
    }

    @DisplayName("Should throw an exception when a film description is more than 200 symbols")
    @Test
    void shouldThrowExceptionWhenFilmDescriptionIsMoreThan200Symbols() {
        Film testFilm = new Film("Name", "a".repeat(201), LocalDate.EPOCH, 1);

        assertThrows(ValidationException.class, () -> this.controller.validate(testFilm));
    }

    @DisplayName("Shouldn't throw an exception when a film description is less than 200 symbols")
    @Test
    void shouldNotThrowExceptionWhenFilmDescriptionIsLessThan200Symbols() {
        Film testFilm = new Film("Name", "a".repeat(200), LocalDate.EPOCH, 1);

        assertDoesNotThrow(() -> this.controller.validate(testFilm));
    }

    @DisplayName("Should throw an exception when an release date is before cinema invention")
    @Test
    void shouldThrowExceptionWhenReleaseDateIsBeforeCinemaInvention() {
        Film testFilm = new Film("Name", "a".repeat(200), LocalDate.parse("1895-12-27"),
                1);

        assertThrows(ValidationException.class, () -> this.controller.validate(testFilm));
    }

    @DisplayName("Shouldn't throw an exception when an release date is after cinema invention")
    @Test
    void shouldNotThrowExceptionWhenReleaseDateIsAfterCinemaInvention() {
        Film testFilm = new Film("Name", "a".repeat(200), LocalDate.parse("1895-12-28"),
                1);

        assertDoesNotThrow(() -> this.controller.validate(testFilm));
    }

    @DisplayName("Should throw a exception when film duration is negative")
    @Test
    void shouldThrowExceptionWhenFilmDurationIsNegative() {
        Film testFilm = new Film("Name", "a".repeat(200), LocalDate.EPOCH,
                -1);

        assertThrows(ValidationException.class, () -> this.controller.validate(testFilm));
    }
}