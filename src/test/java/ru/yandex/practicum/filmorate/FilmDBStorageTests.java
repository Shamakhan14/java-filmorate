package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDBStorageTests {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userStorage;

    @Test
    public void shouldCreateFilm() {
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        assertEquals(film1.getName(), film2.getName());
        assertEquals(film1.getDescription(), film2.getDescription());
        assertEquals(film1.getReleaseDate(), film2.getReleaseDate());
        assertEquals(film1.getDuration(), film2.getDuration());
        assertEquals(film1.getMpa(), film2.getMpa());
    }

    @Test
    public void shouldFindFilm() {
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        Film film3 = filmDbStorage.findFilm(film2.getId());
        assertEquals(film3.getName(), film2.getName());
        assertEquals(film3.getDescription(), film2.getDescription());
        assertEquals(film3.getReleaseDate(), film2.getReleaseDate());
        assertEquals(film3.getDuration(), film2.getDuration());
        assertEquals(film3.getMpa(), film2.getMpa());
    }

    @Test
    public void shouldUpdateFilm() {
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        Film film3 = filmDbStorage.findFilm(film2.getId());
        film3.setName("3name");
        filmDbStorage.updateFilm(film3);
        assertEquals(film2.getId(), film3.getId());
        assertEquals("3name", film3.getName());
        assertEquals(film2.getDescription(), film3.getDescription());
        assertEquals(film2.getReleaseDate(), film3.getReleaseDate());
        assertEquals(film2.getDuration(), film3.getDuration());
        assertEquals(film2.getMpa(), film3.getMpa());
    }

    @Test
    public void shouldGetAllFilms() {
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        assertEquals(1, filmDbStorage.getFilms().size());
    }

    @Test
    public void shouldAddLike() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        filmDbStorage.addLike(film2.getId(), user2.getId());
        assertEquals(1, filmDbStorage.getLikedIDs(film2.getId()).size());
    }

    @Test
    public void shouldRemoveLike() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        filmDbStorage.addLike(film2.getId(), user2.getId());
        assertEquals(1, filmDbStorage.getLikedIDs(film2.getId()).size());
        filmDbStorage.removeLike(film2.getId(), user2.getId());
        assertEquals(0, filmDbStorage.getLikedIDs(film2.getId()).size());
    }

    @Test
    public void shouldGetTopFilms() {
        User user1 = new User("1email@email.com", "login1", "name1", LocalDate.now());
        User user2 = userStorage.addUser(user1);
        Film film1 = new Film("1name", "1description", LocalDate.now(), 100,
                new Rating(1, "G"));
        Film film2 = filmDbStorage.addFilm(film1);
        filmDbStorage.addLike(film2.getId(), user2.getId());
        assertEquals(1, filmDbStorage.getLikedIDs(film2.getId()).size());
        assertEquals(1, filmDbStorage.getTopFilms(1).size());
    }
}
