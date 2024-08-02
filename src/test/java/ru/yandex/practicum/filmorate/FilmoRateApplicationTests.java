package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.film.FilmsRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.mpa.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.user.UserDtoRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class, FilmsRowMapper.class, GenreRowMapper.class, MpaRowMapper.class,
        UserDtoRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final Film testFilm = Film.builder()
            .name("name")
            .description("description")
            .releaseDate(LocalDate.EPOCH)
            .duration(42)
            .build();
    private final Film secondTestFilm = Film.builder()
            .name("secondTestFilmName")
            .description("secondTestFilmDescription")
            .releaseDate(LocalDate.EPOCH.plusYears(1))
            .duration(43)
            .build();
    private final User testUser = User.builder()
            .login("login")
            .name("name")
            .email("mail@email.com")
            .birthday(LocalDate.EPOCH)
            .build();
    private final User secondTestUser = User.builder()
            .login("secondLogin")
            .name("secondName")
            .email("secondMail@email.com")
            .birthday(LocalDate.EPOCH)
            .build();

    @Test
    void testAddUser() {
        Long testUserId = userStorage.addUser(testUser);

        assertEquals(testUserId, userStorage.getUserById(testUserId).getId());
    }

    @Test
    void testAddFriend() {
        Long testUserId = userStorage.addUser(testUser);
        Long secondTestUserId = userStorage.addUser(secondTestUser);

        userStorage.addFriend(testUserId, secondTestUserId);

        assertNotNull(userStorage.getUserById(testUserId).getFriends());
        assertTrue(userStorage.getUserById(testUserId).getFriends().contains(secondTestUserId));
    }

    @Test
    void testFindAllUsers() {
        userStorage.addUser(testUser);
        userStorage.addUser(secondTestUser);

        List<User> users = userStorage.findAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserById() {
        Long testUserId = userStorage.addUser(testUser);

        assertEquals(testUserId, userStorage.getUserById(testUserId).getId());
    }

    @Test
    void testUpdateUser() {
        Long testUserId = userStorage.addUser(testUser);
        User testUpdatedUser = User.builder()
                .id(testUserId)
                .login("updatedLogin")
                .name("updatedName")
                .email("updatedName")
                .birthday(LocalDate.EPOCH.plusYears(1))
                .build();
        ;

        userStorage.updateUser(testUpdatedUser);

        assertEquals(testUpdatedUser.toString(), userStorage.getUserById(testUserId).toString());
    }

    @Test
    void testRemoveFriend() {
        Long testUserId = userStorage.addUser(testUser);
        Long secondTestUserId = userStorage.addUser(secondTestUser);

        userStorage.addFriend(testUserId, secondTestUserId);

        assertNotNull(userStorage.getUserById(testUserId).getFriends());
        assertTrue(userStorage.getUserById(testUserId).getFriends().contains(secondTestUserId));

        userStorage.removeFriend(testUserId, secondTestUserId);

        assertNull(userStorage.getUserById(testUserId).getFriends());
    }

    @Test
    void testCountUsers() {
        userStorage.addUser(testUser);
        userStorage.addUser(secondTestUser);

        assertEquals(2, userStorage.countUsers());
    }

    @Test
    void testAddFilm() {
        Long id = filmStorage.addFilm(testFilm);

        assertEquals(id, filmStorage.getFilmById(id).getId());
    }

    @Test
    void testAddLike() {
        Long testFilmId = filmStorage.addFilm(testFilm);
        Long userId = userStorage.addUser(testUser);

        filmStorage.addLike(testFilmId, userId);

        Set<Long> likes = filmStorage.getFilmById(testFilmId).getUsersLikes();

        assertNotNull(likes);
        assertFalse(likes.isEmpty());
        assertEquals(1, likes.size());
    }

    @Test
    void testGetAllFilms() {
        filmStorage.addFilm(testFilm);
        filmStorage.addFilm(secondTestFilm);

        List<Film> films = filmStorage.getAllFilms();

        assertFalse(films.isEmpty());
        assertEquals(2, films.size());
    }

    @Test
    void testGetFilmById() {
        Long testFilmId = filmStorage.addFilm(testFilm);

        assertEquals(testFilmId, filmStorage.getFilmById(testFilmId).getId());
    }

    @Test
    void testUpdateFilm() {
        Long testFilmId = filmStorage.addFilm(testFilm);
        Film testUpdatedFilm = Film.builder()
                .id(testFilmId)
                .name("updatedName")
                .description("updatedDescription")
                .releaseDate(LocalDate.EPOCH.plusYears(1))
                .duration(43)
                .build();

        filmStorage.updateFilm(testUpdatedFilm);

        assertEquals(testUpdatedFilm.toString(), filmStorage.getFilmById(testFilmId).toString());
    }

    @Test
    void testDeleteLike() {
        Long testUserId = userStorage.addUser(testUser);

        testFilm.setUsersLikes(new HashSet<>(List.of(testUserId)));

        Long testFilmId = filmStorage.addFilm(testFilm);

        filmStorage.deleteLike(testFilmId, testUserId);
        assertNull(filmStorage.getFilmById(testFilmId).getUsersLikes());
    }
}