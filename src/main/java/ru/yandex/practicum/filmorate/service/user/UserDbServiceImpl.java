package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserFriendDto;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        validateUserBirthday(newUser);
        Long id = storage.addUser(newUser);

        newUser.setId(id);
        return UserMapper.mapToUserCreateDto(newUser);
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
                .map(UserMapper::mapToUserCreateDto)
                .get();
    }

    @Override
    public List<UserDto> findAllUsers() {
        return storage.findAllUsers().stream()
                .map(UserMapper::mapToUserCreateDto)
                .toList();
    }

    @Override
    public List<UserFriendDto> findAllUserFriends(Long userId) {
        validateUserId(userId);

        Set<Long> friendIds = storage.getUserById(userId).getFriends();

        if (friendIds != null) {
            return friendIds.stream()
                    .map(friendId -> {
                        UserFriendDto dto = new UserFriendDto();

                        dto.setId(friendId);

                        return dto;
                    })
                    .toList();
        }

        return List.of();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        validateUserId(friendId);
        storage.addFriend(userId, friendId);
    }

    private void validateUserId(Long friendId) {
        if (friendId > storage.countUsers()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no user with an id " + friendId);
        }
    }

    @Override
    public List<UserFriendDto> showCommonFriends(Long userId, Long userIdToCompare) {
        User firstUser = storage.getUserById(userId);
        User userToCompare = storage.getUserById(userIdToCompare);

        Set<Long> intersection = Optional.ofNullable(firstUser.getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " doesn't have friends"));
        Set<Long> secondUserFriends = Optional.ofNullable(userToCompare.getFriends())
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "User with id " + userIdToCompare + " doesn't have friends"));

        intersection.retainAll(secondUserFriends);
        log.info("The list of common fiends user with id {} and user with id {} was created",
                userId, userIdToCompare);

        return intersection.stream()
                .map(commonFriendId -> {
                    UserFriendDto dto = new UserFriendDto();

                    dto.setId(commonFriendId);

                    return dto;
                })
                .toList();
    }

    @Override
    public void removeFriend(Long userId, Long userToUnfriendId) {
        validateUserId(userId);
        validateUserId(userToUnfriendId);

        try {
            storage.removeFriend(userId, userToUnfriendId);
        } catch (InternalServerException e) {
            throw new ValidationException(HttpStatus.OK, e.getMessage());
        }
    }

    private void validateUserBirthday(@NonNull User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }
}
