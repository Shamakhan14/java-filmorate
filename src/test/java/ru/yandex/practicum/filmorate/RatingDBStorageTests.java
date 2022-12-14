package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDBStorageTests {

    private final RatingDbStorage ratingDbStorage;

    @Test
    public void shouldReturnRating() {
        Rating rating = ratingDbStorage.getRating(1);
        assertEquals("G", rating.getName());
    }

    @Test
    public void shouldReturnAllRatings() {
        List<Rating> ratings = ratingDbStorage.getRatings();
        assertEquals(5, ratings.size());
    }
}
