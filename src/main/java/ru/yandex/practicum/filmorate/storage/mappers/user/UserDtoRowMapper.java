package ru.yandex.practicum.filmorate.storage.mappers.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.user.UserRowDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDtoRowMapper implements RowMapper<UserRowDto> {

    @Override
    public UserRowDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserRowDto dto = UserRowDto.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();

        long friendId = resultSet.getLong("friend_id");

        if (friendId != 0) {
            dto.setFriendId(friendId);
        }

        return dto;
    }
}
