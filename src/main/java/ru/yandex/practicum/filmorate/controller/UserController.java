package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateDto;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UserResponseDto addUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("Добавить пользователя {}", userCreateDto);
        UserResponseDto createdUser = userService.addUser(userCreateDto);
        log.info("Добавлен пользователь {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public UserResponseDto updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Обновление пользователя {}", userUpdateDto);
        UserResponseDto updatedUser = userService.updateUser(userUpdateDto);
        log.info("Обновленный пользователь {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Запрос дружбы userId - {}, friendId - {}", id, friendId);
        friendshipService.addFriend(id, friendId);
        log.info("Добавлена дружба userId - {}, friendId - {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Удаление из друзей userId - {}, friendId - {}", id, friendId);
        friendshipService.removeFriend(id, friendId);
        log.info("Удалён из друзей userId - {}, friendId - {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserResponseDto> getFriends(@PathVariable Integer id) {
        log.info("Получаем друзей userId - {}", id);
        return friendshipService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<UserResponseDto> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получаем друзей для userId - {}, otherId - {}", id, otherId);
        return friendshipService.getCommonFriends(id, otherId);
    }
}