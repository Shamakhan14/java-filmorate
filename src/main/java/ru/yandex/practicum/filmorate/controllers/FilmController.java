package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private FilmStorage filmStorage;
    private FilmService filmService;
    private UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (filmService.isValid(film)) {
            filmStorage.addFilm(film);
            log.info("Фильм " + film.getName() + " успешно добавлен.");
        }
        return film;
    }

    @GetMapping("/films")
    @ResponseBody
    public List<Film> getAllFilms() {
        log.info("Запрошен список фильмов.");
        return filmStorage.getFilms();
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        boolean contains = false;
        for (Film film1: filmStorage.getFilms()) {
            if (film1.getId() == film.getId()) {
                contains = true;
                break;
            }
        }
        if (contains && filmService.isValid(film)) {
            filmStorage.updateFilm(film);
            log.info("Фильм " + film.getName() + " успешно обновлен.");
            return film;
        } else {
            throw new ObjectNotFoundException("Ошибка при обновлении фильма.");
        }
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable int id) {
        if (filmStorage.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        return filmStorage.findFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        if (filmStorage.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        if (userStorage.findUser(userId) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        filmService.addLike(id, userId);
        return filmStorage.findFilm(id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        if (filmStorage.findFilm(id) == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        if (userStorage.findUser(userId) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        if (!filmStorage.findFilm(id).getLikedIDs().contains(userId))
            throw new ObjectNotFoundException("Пользователю уже не нравится данный фильм.");
        filmService.removeLike(id, userId);
        return filmStorage.findFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) String count) {
        int top = 10;
        if (count != null) top = Integer.parseInt(count);
        if (top > filmStorage.getFilms().size()) top = filmStorage.getFilms().size();
        if (top == 0) throw new ValidationException("Неверно указано количество фильмов.");
        return filmService.getTopFilms(top);
    }
}
