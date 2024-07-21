package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(User newUser);

    UserDto updateUser(User updatedUser);

    List<UserDto> findAllUsers();

    List<UserDto> findAllUserFriends(long userId);

    void addFriend(long userId, long newFriendId);

    List<UserDto> showCommonFriends(long userId, long userIdToCompare);

    UserDto unfriend(long userId, long userToUnfriendId);
}
