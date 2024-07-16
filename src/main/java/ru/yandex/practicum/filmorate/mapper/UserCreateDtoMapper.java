package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public class UserCreateDtoMapper {

    private UserCreateDtoMapper() {
    }

    public static UserCreateDto maptoUserCreateDto(User specificUser) {
        UserCreateDto dto = new UserCreateDto();
        Set<Long> friends = specificUser.getFriends();

        dto.setId(specificUser.getId());
        dto.setEmail(specificUser.getEmail());
        dto.setLogin(specificUser.getLogin());
        dto.setName(specificUser.getName());
        dto.setBirthday(specificUser.getBirthday());

        if (friends !=null) {
            dto.setFriends(friends);
        }
        return dto;
    }
}