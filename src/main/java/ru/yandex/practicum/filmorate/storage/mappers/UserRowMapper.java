package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User mappedUser = new User();
        Set<Long> friends = new HashSet<>();

        mappedUser.setId(resultSet.getLong("id"));
        mappedUser.setEmail(resultSet.getString("email"));
        mappedUser.setName(resultSet.getString("user_name"));
        mappedUser.setLogin(resultSet.getString("login"));
        mappedUser.setBirthday(resultSet.getDate("birthday").toLocalDate());

        while(resultSet.next()) {
            friends.add(resultSet.getLong("friends"));
        }

        mappedUser.setFriends(friends);
        return mappedUser;
    }
}
