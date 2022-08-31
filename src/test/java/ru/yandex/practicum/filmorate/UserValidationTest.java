package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    InMemoryUserStorage userStorage;
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    public void shouldValidateUsualUser() {
        User user = new User("heman@man.ru", "login", "name", LocalDate.now().minusYears(20));
        assertTrue(userService.isValid(user));
    }

    @Test
    public void shouldValidateWithEmptyName() {
        User user = new User("heman@man.ru", "login", "", LocalDate.now().minusYears(20));
        assertTrue(userService.isValid(user));
    }

    @Test
    public void shouldNotValidateWithEmptyEmail() {
        User user = new User("", "login", "name", LocalDate.now().minusYears(20));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateWithWrongEmail() {
        User user = new User("heman", "login", "name", LocalDate.now().minusYears(20));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateWithEmptyLogin() {
        User user = new User("heman@mail.ru", "", "name", LocalDate.now().minusYears(20));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateWhenLoginContainsSpace() {
        User user = new User("heman@mail.ru", "heman@ mail.ru", "name", LocalDate.now().minusYears(20));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateWhenDateIsAfterNow() {
        User user = new User("heman@mail.ru", "login", "name", LocalDate.now().plusYears(20));
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateUserWithNullEmail() {
        User user = new User(null, "login", "name", LocalDate.now());
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateUserWithNullLogin() {
        User user = new User("heman@mail.ru", null, "name", LocalDate.now());
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }

    @Test
    public void shouldNotValidateUserWithNullDate() {
        User user = new User("heman@mail.ru", "login", "name", null);
        assertThrows(ValidationException.class, () -> userService.isValid(user));
    }
}
