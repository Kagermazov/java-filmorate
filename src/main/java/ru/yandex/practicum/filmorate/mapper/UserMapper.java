package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto maptoUserCreateDto(User specificUser) {
        UserDto dto = new UserDto();
        Set<Long> friends = specificUser.getFriends();

        dto.setId(specificUser.getId());
        dto.setEmail(specificUser.getEmail());
        dto.setLogin(specificUser.getLogin());
        dto.setName(specificUser.getName());
        dto.setBirthday(specificUser.getBirthday());

        if (friends != null) {
            dto.setFriends(friends);
        }
        return dto;
    }
}
