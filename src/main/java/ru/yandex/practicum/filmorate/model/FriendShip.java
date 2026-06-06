package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendShip {
    private Long userId;

    private Long friendId;

    private String status;
}
