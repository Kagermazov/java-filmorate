package ru.yandex.practicum.filmorate.storage.mappers.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User mappedUser = new User();
        Set<Long> friends = new HashSet<>();

        while(resultSet.next()) {
            mappedUser.setId(resultSet.getLong("id"));
            mappedUser.setLogin(resultSet.getString("login"));
            mappedUser.setName(resultSet.getString("user_name"));
            mappedUser.setEmail(resultSet.getString("email"));
            mappedUser.setBirthday(resultSet.getDate("birthday").toLocalDate());
        }

        friends.add(resultSet.getLong("friend_id"));
        mappedUser.setFriends(friends);
        return mappedUser;
    }
}