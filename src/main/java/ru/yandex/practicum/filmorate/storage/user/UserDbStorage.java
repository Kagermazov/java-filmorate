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
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository<UserRowDto> implements UserStorage {
    private static final String ADD_USER_QUERY =
            "INSERT INTO users (email, user_name, login, birthday) VALUES (?, ?, ?, ?)";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?);";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
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
    private static final String GET_USER_BY_ID_QUERY = "SELECT u.*, " +
            "f.friend_id " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id " +
            "WHERE u.id = ?;";
    private static final String COUNT_USERS_QUERY = "SELECT COUNT(id) FROM users;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?;";

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
        List<UserRowDto> users = findMany(GET_ALL_USER_QUERY);
        Map<Long, List<UserRowDto>> userIdToUser = users.stream().collect(groupingBy(UserRowDto::getId));

        return userIdToUser.values().stream()
                .map(this::combineRows)
                .toList();
    }

    private User combineRows(List<UserRowDto> users) {
        UserRowDto firstDto = users.getFirst();

        return User.builder()
                .id(firstDto.getId())
                .login(firstDto.getLogin())
                .name(firstDto.getName())
                .email(firstDto.getEmail())
                .birthday(firstDto.getBirthday())
                .friends(users.stream()
                        .map(UserRowDto::getFriendId)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        insert(ADD_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        update(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public User getUserById(Long userId) {
        List<UserRowDto> dtos = findMany(GET_USER_BY_ID_QUERY, userId);

        if (dtos.isEmpty()) {
            return null;
        }

        UserRowDto firstDto = dtos.getFirst();

        User expectedUser = User.builder()
                .id(firstDto.getId())
                .login(firstDto.getLogin())
                .name(firstDto.getName())
                .email(firstDto.getEmail())
                .birthday(firstDto.getBirthday())
                .build();

        Set<Long> friends = dtos.stream()
                .map(UserRowDto::getFriendId)
                .collect(Collectors.toSet());

        if (!friends.contains(null)) {
            expectedUser.setFriends(friends);
        }

        return expectedUser;
    }

    public Long countUsers() {
        return countRows(COUNT_USERS_QUERY);
    }

    public void deleteUser(Long id) {
        update(DELETE_USER_QUERY, id);
    }
}
