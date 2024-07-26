package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto mapToUserCreateDto(User specificUser) {
        UserDto dto = UserDto.builder()
                .id(specificUser.getId())
                .email(specificUser.getEmail())
                .login(specificUser.getLogin())
                .name(specificUser.getName())
                .birthday(specificUser.getBirthday())
                .build();

        Set<Long> friends = specificUser.getFriends();

        if (friends != null) {
            dto.setFriends(friends);
        }

        return dto;
    }
}
