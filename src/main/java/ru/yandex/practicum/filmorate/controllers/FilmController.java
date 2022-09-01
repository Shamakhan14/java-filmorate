package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private FilmService filmService;
    private UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (filmService.isValid(film)) {
            filmService.addFilm(film);
            log.info("Фильм " + film.getName() + " успешно добавлен.");
        }
        return film;
    }

    @GetMapping("/films")
    @ResponseBody
    public List<Film> getAllFilms() {
        log.info("Запрошен список фильмов.");
        return filmService.getFilms();
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        boolean contains = false;
        for (Film film1: filmService.getFilms()) {
            if (film1.getId() == film.getId()) {
                contains = true;
                break;
            }
        }
        if (contains && filmService.isValid(film)) {
            filmService.updateFilm(film);
            log.info("Фильм " + film.getName() + " успешно обновлен.");
            return film;
        } else {
            throw new ObjectNotFoundException("Ошибка при обновлении фильма.");
        }
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable int id) {
        if (filmService.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        return filmService.findFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        if (userService.findUser(userId) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        filmService.addLike(id, userId);
        return filmService.findFilm(id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        if (userService.findUser(userId) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        if (!filmService.findFilm(id).getLikedIDs().contains(userId))
            throw new ObjectNotFoundException("Пользователю уже не нравится данный фильм.");
        filmService.removeLike(id, userId);
        return filmService.findFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) throw new ValidationException("Неверно указано количество фильмов.");
        if (count > filmService.getFilms().size()) count = filmService.getFilms().size();
        return filmService.getTopFilms(count);
    }
}
