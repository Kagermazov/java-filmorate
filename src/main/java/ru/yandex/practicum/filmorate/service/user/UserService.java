package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(User newUser);

    UserDto updateUser(User updatedUser);

    List<UserDto> findAllUsers();

    List<UserDto> findAllUserFriends(Long userId);

    void addFriend(Long userId, Long newFriendId);

    List<UserDto> showCommonFriends(Long userId, Long userIdToCompare);

    UserDto unfriend(Long userId, Long userToUnfriendId);
}
