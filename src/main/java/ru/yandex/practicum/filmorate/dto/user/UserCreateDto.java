package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.validation.annotation.NoSpacesLogin;

@Data
public class UserCreateDto {

    @NotBlank
    @NoSpacesLogin
    private String login;

    @NotBlank
    @Email
    private String email;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;
}