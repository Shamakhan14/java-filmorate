package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Service.RatingService;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/mpa/{id}")
    public Rating getRating(@PathVariable int id) {
        log.info("Запрошен MPA рейтинг с id = " + id + ".");
        return ratingService.getRating(id);
    }

    @GetMapping("/mpa")
    public List<Rating> getRatings() {
        log.info("Запрошен список MPA рейтингов.");
        return ratingService.getRatings();
    }
}
