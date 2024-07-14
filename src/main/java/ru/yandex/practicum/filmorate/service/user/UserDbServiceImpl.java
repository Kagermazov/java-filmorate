package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
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
    public UserCreateDto addUser(User newUser) {
        validate(newUser);
        long id = this.storage.addUser(newUser);

        newUser.setId(id);
        return UserCreateDtoMapper.maptoUserCreateDto(newUser);
    }

    @Override
    public UserCreateDto updateUser(User updatedUser) {
        return null;
    }

    @Override
    public List<UserCreateDto> findAllUsers() {
        return this.storage.findAllUsers().stream()
                .map(UserCreateDtoMapper::maptoUserCreateDto)
                .toList();
    }

    @Override
    public List<UserCreateDto> findAllUserFriends(long userId) {
        return List.of();
    }

    @Override
    public UserCreateDto addFriend(long userId, long newFriendId) {
        return null;
    }

    @Override
    public List<UserCreateDto> showCommonFriends(long userId, long userIdToCompare) {
        return List.of();
    }

    @Override
    public UserCreateDto unfriend(long userId, long userToUnfriendId) {
        return null;
    }

    private void validate(@NonNull User userToCheck) {
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.warn("The new user's birthday is in the future");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The birthday is wrong");
        }
    }
}
