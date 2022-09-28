package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (isValid(film)) {
            filmService.addFilm(film);
            log.info("Фильм " + film.getName() + " успешно добавлен.");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Запрошен список фильмов.");
        return filmService.getFilms();
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (isValid(film)) {
            filmService.updateFilm(film);
            log.info("Фильм " + film.getName() + " успешно обновлен.");
            return film;
        } else {
            throw new ObjectNotFoundException("Ошибка при обновлении фильма.");
        }
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Запрошен фильм " + filmService.findFilm(id).getName() + ".");
        return filmService.findFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.info("Пользователь " + userService.findUser(userId).getName() + " поставил фильму " +
                filmService.findFilm(id).getName() + " лайк.");
        return filmService.findFilm(id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователю " + userService.findUser(userId).getName() + " больше не нравится фильм " +
                filmService.findFilm(id).getName() + ".");
        return filmService.findFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Запрошен топ " + count + " фильмов.");
        return filmService.getTopFilms(count);
    }

    private boolean isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank() ||
                film.getDescription() == null || film.getDescription().length() > 200 ||
                film.getReleaseDate() == null || film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE) ||
                film.getDuration() <= 0) {
            throw new ValidationException("Неверно введены данные о фильме.");
        }
        return true;
    }
}
