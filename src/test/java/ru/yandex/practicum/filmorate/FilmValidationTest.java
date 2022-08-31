package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    InMemoryFilmStorage filmStorage;
    FilmService filmService;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    public void shouldValidateUsualFilm() {
        Film film = new Film("name", "description", LocalDate.now(), 100);
        assertTrue(filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithEmptyName() {
        Film film = new Film("", "description", LocalDate.now(), 100);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithLongDescription() {
        Film film = new Film("name", "descriptionksmc;lkdm';alsmdv';alkms'dkmv'a;skfm';aklmd;f'kbm'" +
                ";kdm'va;ksm'v;las'f;lmva';skldmv';lams'd;lm'a;ksjgkan';kvm'a;ksd'gkan';fkvm'a;skdm';gkna's;kdn';a" +
                "dpasojgiahorjgnalkjfnvalkjns;jgba;ljsndv;lajsn;ljba;ljdsnb;lgjanb;sdjlnf;ljabs;ldjgb;aljsdb;ljgba" +
                "akdlsng;lajnsd;ljab;sdjlb;ljkans;dlfkna;jlsdbg;lajsn;lgkjan;sdkj;fljah;isna;vlksn;ljkna;lsdnnjknjk" +
                "lkn;lkjan;lkdsn;lfjhaoseihrnaf;sfnaoseihfoansdovnaosdnfpoiansdovanso", LocalDate.now(), 100);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithWrongDate() {
        Film film = new Film("name", "description", LocalDate.of(1001, 1, 2), 100);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldValidateFilmWithExactlyWrongDate() {
        Film film = new Film("name", "description", LocalDate.of(1895, 12, 28), 100);
        assertTrue(filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithDuration0() {
        Film film = new Film("name", "description", LocalDate.now(), 0);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithDurationLessThan0() {
        Film film = new Film("name", "description", LocalDate.now(), -10);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithNullName() {
        Film film = new Film(null, "description", LocalDate.now(), 200);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldBotValidateFilmWithNullDescription() {
        Film film = new Film("name", null, LocalDate.now(), 200);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }

    @Test
    public void shouldNotValidateFilmWithNullDate() {
        Film film = new Film("name", "description", null, 200);
        assertThrows(ValidationException.class, () -> filmService.isValid(film));
    }
}
