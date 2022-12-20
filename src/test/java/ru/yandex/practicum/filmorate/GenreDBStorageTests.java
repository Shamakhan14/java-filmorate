package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDBStorageTests {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void shouldReturnGenre() {
        Genre genre = genreDbStorage.getGenre(1);
        assertEquals("Комедия", genre.getName());
    }

    @Test
    public void shouldReturnAllGenres() {
        List<Genre> genres = genreDbStorage.getGenres();
        assertEquals(6, genres.size());
    }
}
