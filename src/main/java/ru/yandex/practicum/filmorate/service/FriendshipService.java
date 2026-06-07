package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.UserDtoMapper;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendshipService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;
    private final UserDtoMapper userDtoMapper;

    public FriendshipService(FriendshipStorage friendshipStorage, UserStorage userStorage) {
        this.friendshipStorage = friendshipStorage;
        this.userStorage = userStorage;
        this.userDtoMapper = new UserDtoMapper();
    }

    public void addFriend(Integer userId, Integer friendId) {
        log.debug("Добавлена дружба между пользователями  {} и {}", userId, friendId);
        Integer user1Id = Integer.min(userId, friendId);
        Integer user2Id = Integer.max(userId, friendId);
        validateUserExists(userId);
        validateUserExists(friendId);
        Optional<Friendship> exist = friendshipStorage.findFriendship(user1Id, user2Id);

        if (exist.isPresent()) {
            Friendship friendship = exist.get();
            FriendshipStatus friendshipStatus = friendship.getStatus();
            log.debug("Дружба существует");
            log.debug("Статус дружбы - {}", friendshipStatus);

            if (friendship.getStatus().equals(FriendshipStatus.PENDING)) {
                if (friendship.getRequesterId().equals(friendId)) {
                    friendship.setStatus(FriendshipStatus.CONFIRMED);
                    friendshipStorage.updateFriendshipStatus(friendshipStatus,
                            friendship.getUser1Id(),
                            friendship.getUser2Id());
                }
            }
        } else {
            log.debug("Добавление новой дружбы между пользователями {} и {}", userId, friendId);
            Friendship friendship = new Friendship();
            friendship.setUser1Id(userId);
            friendship.setUser2Id(friendId);
            friendship.setRequesterId(userId);
            friendship.setStatus(FriendshipStatus.PENDING);
            friendshipStorage.addFriendship(friendship);
        }
    }

    public void removeFriend(Integer userId, Integer friendId) {
        log.debug("Удаление дружбы между пользователями {} и {}", userId, friendId);
        validateUserExists(userId);
        validateUserExists(friendId);
        Optional<Friendship> exist = friendshipStorage.findFriendship(userId, friendId);
        exist.ifPresent(friendshipStorage::removeFriendship);
    }

    public Collection<UserResponseDto> getFriends(Integer userId) {
        log.debug("Получить друзей пользователя {}", userId);
        validateUserExists(userId);
        List<User> friends = friendshipStorage.getFriends(userId);
        if (!friends.isEmpty()) {
            log.debug("Кол-во друзей - {}", friends.size());
            return friends.stream().map(userDtoMapper::toUserResponseDto)
                    .collect(Collectors.toList());
        }
        log.debug("Нет друзей у пользователя - {}", userId);
        return new ArrayList<>();
    }

    public Collection<UserResponseDto> getCommonFriends(Integer userId, Integer friendId) {
        log.debug("Получение общих пользователей между {} и {}", userId, friendId);

        if (Objects.equals(userId, friendId)) {
            log.warn("Получение общих друзей от одного и того пользователя");
            return new ArrayList<>();
        }

        validateUserExists(userId);
        validateUserExists(friendId);

        List<User> commonFriends = friendshipStorage.getCommonFriendsId(userId, friendId);
        if (!commonFriends.isEmpty()) {
            log.debug("Кол-во общих друзей - {}", commonFriends.size());
            return commonFriends.stream().map(userDtoMapper::toUserResponseDto)
                    .collect(Collectors.toList());
        }
        log.debug("Нет общих друзей между пользователями {} и {}", userId, friendId);
        return new ArrayList<>();
    }

    private void validateUserExists(Integer userId) {
        if (!userStorage.existsUserById(userId)) {
            log.warn("Пользователь {} не найден", userId);
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        }
    }
}