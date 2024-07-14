package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    UserCreateDto addUser(User newUser);

    UserCreateDto updateUser(User updatedUser);

    List<UserCreateDto> findAllUsers();

    List<UserCreateDto> findAllUserFriends(long userId);

    UserCreateDto addFriend(long userId, long newFriendId);

    List<UserCreateDto> showCommonFriends(long userId, long userIdToCompare);

    UserCreateDto unfriend(long userId, long userToUnfriendId);
}
