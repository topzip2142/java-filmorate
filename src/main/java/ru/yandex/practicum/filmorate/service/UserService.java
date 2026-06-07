package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateDto;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.UserDtoMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserDtoMapper userDtoMapper;

    public UserService(FriendshipStorage friendshipStorage,
                       UserStorage userStorage,
                       UserDtoMapper userDtoMapper) {
        this.userStorage = userStorage;
        this.userDtoMapper = userDtoMapper;
    }

    public UserResponseDto getUserById(Integer userId) {
        log.debug("Получить пользователя {}", userId);

        return userDtoMapper.toUserResponseDto(
                userStorage.getUserById(userId)
                        .orElseThrow(() -> {
                            log.warn("Пользователь {} не найден", userId);
                            return new UserNotFoundException("Пользователь " + userId + " не найден");
                        }));
    }

    public Collection<UserResponseDto> getUsers() {
        log.debug("Получить всех пользователей");

        Collection<UserResponseDto> usersResponseDto = userStorage.getUsers().stream()
                .map(userDtoMapper::toUserResponseDto)
                .toList();
        log.debug("Кол-во пользователей - {}", usersResponseDto.size());
        return usersResponseDto;
    }

    public UserResponseDto addUser(UserCreateDto dto) {
        log.debug("Добавляем пользователя {}", dto);
        String login = dto.getLogin();
        dto.setName((dto.getName() == null || dto.getName().isBlank() ? login : dto.getName()));
        return userDtoMapper.toUserResponseDto(userStorage.addUser(userDtoMapper.toUser(dto)));
    }

    public UserResponseDto updateUser(UserUpdateDto dto) {
        User userToUpdate = userStorage.getUserById(dto.getId())
                .orElseThrow(() -> {
                    log.warn("Пользователь {} не найден", dto.getId());
                    return new UserNotFoundException("Пользователь " + dto.getId() + " не найден");
                });

        if (dto.getLogin() != null && !dto.getLogin().isEmpty()) userToUpdate.setLogin(dto.getLogin());
        if (dto.getName() != null) userToUpdate.setName(dto.getName());
        if (dto.getEmail() != null) userToUpdate.setEmail(dto.getEmail());
        if (dto.getBirthday() != null) userToUpdate.setBirthday(dto.getBirthday());

        User updated = userStorage.updateUser(userToUpdate);
        log.debug("Обновленный пользователь {}", updated);

        return userDtoMapper.toUserResponseDto(updated);
    }
}