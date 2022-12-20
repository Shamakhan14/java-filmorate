package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres/{id}")
    public Genre getRating(@PathVariable int id) {
        log.info("Запрошен жанр с id = " + id + ".");
        return genreService.getGenre(id);
    }

    @GetMapping("/genres")
    public List<Genre> getRatings() {
        log.info("Запрошен список жанров.");
        return genreService.getGenres();
    }
}
