package ru.yandex.practicum.filmorate.storage.mappers.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.user.UserRowDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<UserRowDto> {

    @Override
    public UserRowDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserRowDto dto = new UserRowDto();

        dto.setId(resultSet.getLong("id"));
        dto.setLogin(resultSet.getString("login"));
        dto.setName(resultSet.getString("user_name"));
        dto.setEmail(resultSet.getString("email"));
        dto.setBirthday(resultSet.getDate("birthday").toLocalDate());
        dto.setFriendId(resultSet.getLong("friend_id"));
        return dto;
    }
}
