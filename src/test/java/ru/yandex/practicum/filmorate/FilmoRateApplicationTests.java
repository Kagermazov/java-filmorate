package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
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
@Import({User.class, UserDbStorage.class, FilmDbStorage.class, FilmsRowMapper.class, GenreRowMapper.class,
        MpaRowMapper.class,
        UserDtoRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private static final User testUser = new User();
    private static final User secondTestUser = new User();
    private static final Film testFilm = new Film();
    private static final Film secondTestFilm = new Film();

    @BeforeAll
    static void beforeAll() {
        testUser.setLogin("login");
        testUser.setName("name");
        testUser.setEmail("mail@email.com");
        testUser.setBirthday(LocalDate.EPOCH);

        secondTestUser.setLogin("secondUserLogin");
        secondTestUser.setName("secondUserName");
        secondTestUser.setEmail("secondUserEmail@email.com");
        secondTestUser.setBirthday(LocalDate.EPOCH.plusYears(1));

        testFilm.setName("name");
        testFilm.setDescription("description");
        testFilm.setReleaseDate(LocalDate.EPOCH);
        testFilm.setDuration(42);

        secondTestFilm.setName("secondTestFilmName");
        secondTestFilm.setDescription("secondTestFilmDescription");
        secondTestFilm.setReleaseDate(LocalDate.EPOCH.plusYears(1));
        secondTestFilm.setDuration(43);
    }

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
        User testUpdatedUser = new User();

        testUpdatedUser.setId(testUserId);
        testUpdatedUser.setLogin("updatedLogin");
        testUpdatedUser.setName("updatedName");
        testUpdatedUser.setEmail("updatedEmail@email.com");
        testUpdatedUser.setBirthday(LocalDate.EPOCH.plusYears(1));

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

        filmStorage.addLike(testFilmId,userId);

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
        Film testUpdatedFilm = new Film();

        testUpdatedFilm.setId(testFilmId);
        testUpdatedFilm.setName("updatedName");
        testUpdatedFilm.setDescription("updatedDescription");
        testUpdatedFilm.setReleaseDate(LocalDate.EPOCH.plusYears(1));
        testUpdatedFilm.setDuration(43);

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