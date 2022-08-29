package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private static int ids = 0;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (isValid(film)) {
            film.setId(++ids);
            films.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " успешно добавлен.");
        }
        return film;
    }

    @GetMapping("/films")
    @ResponseBody
    public List<Film> getAllFilms() {
        log.info("Запрошен список фильмов.");
        return new ArrayList<>(films.values());
    }

    @PutMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId()) && isValid(film)) {
            films.put(film.getId(), film);
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
