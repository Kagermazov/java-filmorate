package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.user.UserRowDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository<UserRowDto> implements UserStorage {
    private static final String ADD_USER_QUERY =
            "INSERT INTO users (email, user_name, login, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users " +
            "SET login = ?, " +
            "user_name = ?, " +
            "email = ?, " +
            "birthday = ? " +
            "WHERE id = ?";
    private static final String GET_ALL_USER_QUERY = "SELECT u.*, " +
            "f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id;";
    private static final String GET_USER_BY_ID = "SELECT u.*, " +
            "f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id " +
            "WHERE u.id = ?;";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?);";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<UserRowDto> mapper) {
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
                        insert(ADD_FRIEND_QUERY, newUser.getId(), friendId);
                    }
                });

        return id;
    }

    @Override
    public void updateUser(User updatedUser) {
        update(UPDATE_USER_QUERY,
                updatedUser.getLogin(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getBirthday(),
                updatedUser.getId());

    }

    @Override
    public List<User> findAllUsers() {
        List<UserRowDto> users = findMany(GET_ALL_USER_QUERY, mapper);
        Map<Long, List<UserRowDto>> userIdToUser = users.stream().collect(groupingBy(UserRowDto::getId));

        return userIdToUser.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private User combineRows(List<UserRowDto> users) {
        User userToReturn = new User();
        UserRowDto firstDto = users.getFirst();

        userToReturn.setId(firstDto.getId());
        userToReturn.setLogin(firstDto.getLogin());
        userToReturn.setName(firstDto.getName());
        userToReturn.setEmail(firstDto.getEmail());
        userToReturn.setBirthday(firstDto.getBirthday());

        userToReturn.setFriends(
                users.stream()
                        .map(UserRowDto::getFriendId)
                        .collect(Collectors.toSet()));

        return userToReturn;
    }

    public User addFriend(Long userId, Long friendId) {
        insert(ADD_FRIEND_QUERY, userId, friendId);
        return null;
    }

    @Override
    public User getAllFriends(Long userId) {
        return null;
    }
}
