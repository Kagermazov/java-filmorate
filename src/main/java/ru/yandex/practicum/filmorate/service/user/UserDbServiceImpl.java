package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserCreateDtoMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service("userDbServiceImpl")
public class UserDbServiceImpl implements UserService{
    private final UserStorage storage;

    @Autowired
    public UserDbServiceImpl(@Qualifier("userDbStorage")UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto addUser(User newUser) {
        validate(newUser);
        long id = this.storage.addUser(newUser);

        newUser.setId(id);
        return UserCreateDtoMapper.maptoUserCreateDto(newUser);
    }

    @Override
    public UserDto updateUser(User updatedUser) {
        Long updatedUserId = updatedUser.getId();
        List<Long> usersId = this.storage.findAllUsers().stream()
                .map(User::getId)
                .toList();

        if (!usersId.contains(updatedUserId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no user with id " + updatedUserId);
        }

        this.storage.updateUser(updatedUser);

        log.info("The user with an id {} was updated", updatedUserId);

        return this.storage.findAllUsers().stream()
                .filter(user -> user.getId().equals(updatedUserId))
                .findFirst()
                .map(UserCreateDtoMapper::maptoUserCreateDto)
                .get();
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("The user list was created");
        return this.storage.findAllUsers().stream()
                .map(UserCreateDtoMapper::maptoUserCreateDto)
                .toList();
    }

    @Override
    public List<UserDto> findAllUserFriends(long userId) {
        return List.of();
    }

    @Override
    public UserDto addFriend(long userId, long newFriendId) {
        return null;
    }

    @Override
    public List<UserDto> showCommonFriends(long userId, long userIdToCompare) {
        return List.of();
    }

    @Override
    public UserDto unfriend(long userId, long userToUnfriendId) {
        return null;
    }

    private void validate(@NonNull User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }
}
