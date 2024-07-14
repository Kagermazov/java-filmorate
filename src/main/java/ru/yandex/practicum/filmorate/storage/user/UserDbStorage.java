package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String ADD_USER_QUERY =
            "INSERT INTO users (email, user_name, login, birthday) VALUES (?, ?, ?, ?)";
    private static final String ADD_TO_FRIENDS_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String GET_ALL_USER_QUERY = "SELECT u.id AS user_id, " +
            "u.login, " +
            "u.user_name, " +
            "u.email, " +
            "u.birthday, " +
            "f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id;";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public long addUser(User newUser) {
        String userName = newUser.getName();

        if (newUser.getName() == null) {
            userName = newUser.getLogin();
        }

        long id = insert(ADD_USER_QUERY,
                newUser.getEmail(),
                userName,
                newUser.getLogin(),
                newUser.getBirthday());

        Optional.ofNullable(newUser.getFriends())
                .ifPresent(friends -> {
                    for (Long friendId : friends) {
                        insert(ADD_TO_FRIENDS_QUERY, newUser.getId(), friendId);
                    }
                });
        return id;
    }

    @Override
    public void updateUser(User updatedUser) {

    }

    @Override
    public List<User> findAllUsers() {
        return jdbc.query(GET_ALL_USER_QUERY, mapper);
    }
}
