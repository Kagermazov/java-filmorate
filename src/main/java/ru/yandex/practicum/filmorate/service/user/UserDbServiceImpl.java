package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("userDbServiceImpl")
public class UserDbServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserDbServiceImpl(@Qualifier("userDbStorage") UserStorage storageDb) {
        storage = storageDb;
    }

    @Override
    public UserDto addUser(User newUser) {
        validate(newUser);
        Long id = storage.addUser(newUser);

        newUser.setId(id);
        return UserMapper.maptoUserCreateDto(newUser);
    }

    @Override
    public UserDto updateUser(User updatedUser) {
        Long updatedUserId = updatedUser.getId();
        List<Long> usersId = storage.findAllUsers().stream()
                .map(User::getId)
                .toList();

        if (!usersId.contains(updatedUserId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no user with id " + updatedUserId);
        }

        storage.updateUser(updatedUser);

        log.info("The user with an id {} was updated", updatedUserId);

        return storage.findAllUsers().stream()
                .filter(user -> user.getId().equals(updatedUserId))
                .findFirst()
                .map(UserMapper::maptoUserCreateDto)
                .get();
    }

    @Override
    public List<UserDto> findAllUsers() {
        return storage.findAllUsers().stream()
                .map(UserMapper::maptoUserCreateDto)
                .toList();
    }

    @Override
    public List<Long> findAllUserFriends(Long userId) {
        Set<Long> friendIds = storage.getUserById(userId).getFriends();

        if (friendIds != null) {
            return friendIds.stream().toList();
        }

        return List.of();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        storage.addFriend(userId, friendId);
    }

    @Override
    public List<UserDto> showCommonFriends(Long userId, Long userIdToCompare) {
        return List.of();
    }

    @Override
    public void removeFriend(Long userId, Long userToUnfriendId) {
        storage.removeFriend(userId, userToUnfriendId);
    }

    private void validate(@NonNull User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }
}
