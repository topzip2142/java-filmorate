package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Collection<FilmResponseDto> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public FilmResponseDto addFilm(@Valid @RequestBody FilmCreateDto filmCreateDto) {
        log.info("Добавление фильма {}", filmCreateDto);
        FilmResponseDto addFilm = filmService.addFilm(filmCreateDto);
        log.info("Фильм добавлен {}", addFilm);
        return addFilm;
    }

    @PutMapping
    public FilmResponseDto updateFilm(@Valid @RequestBody FilmUpdateDto filmUpdateDto) {
        log.info("Обновление фильма {}", filmUpdateDto);
        FilmResponseDto updatedFilm = filmService.updateFilm(filmUpdateDto);
        log.info("Фильм обновлён {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка фильму c id - {} пользователем - {}", id, userId);

        filmService.addLikeToFilm(id, userId);

        log.info("Лайк добавлен к фильму - {} пользователем - {}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка к фильму - {} пользователем - {}", id, userId);

        filmService.removeLikeFromFilm(id, userId);

        log.info("Like deleted, filmId - {}, userId - {}", id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmResponseDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Топ 10 самых популярных фильмов (кол-во: {})", count);
        return filmService.getPopularFilms(count);
    }

}