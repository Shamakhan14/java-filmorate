package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingStorage ratingStorage;

    public Rating getRating(Integer id) {
        Rating rating = ratingStorage.getRating(id);
        if (rating == null) throw new ObjectNotFoundException("Неверный ID фильма.");
        return rating;
    }

    public List<Rating> getRatings() {
        return ratingStorage.getRatings();
    }
}
