package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private FilmStorage filmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        filmStorage = inMemoryFilmStorage;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (isValid(film)) {
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
    public Film addFilm(@RequestBody Film film) {
        boolean contains = false;
        for (Film film1: filmStorage.getFilms()) {
            if (film1.getId() == film.getId()) {
                contains = true;
                break;
            }
        }
        if (contains && isValid(film)) {
            filmStorage.updateFilm(film);
            log.info("Фильм " + film.getName() + " успешно обновлен.");
            return film;
        } else {
            throw new ValidationException("Ошибка при обновлении фильма.");
        }
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
