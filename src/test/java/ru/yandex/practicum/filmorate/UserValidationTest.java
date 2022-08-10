package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    UserController userController = new UserController();

    @Test
    public void shouldValidateUsualUser() {
        User user = new User("heman@man.ru", "login", "name", LocalDate.now().minusYears(20));
        assertTrue(userController.isValid(user));
    }

    @Test
    public void shouldValidateWithEmptyName() {
        User user = new User("heman@man.ru", "login", "", LocalDate.now().minusYears(20));
        assertTrue(userController.isValid(user));
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void shouldNotValidateWithEmptyEmail() {
        User user = new User("", "login", "name", LocalDate.now().minusYears(20));
        assertFalse(userController.isValid(user));
    }

    @Test
    public void shouldNotValidateWithWrongEmail() {
        User user = new User("heman", "login", "name", LocalDate.now().minusYears(20));
        assertFalse(userController.isValid(user));
    }

    @Test
    public void shouldNotValidateWithEmptyLogin() {
        User user = new User("heman@mail.ru", "", "name", LocalDate.now().minusYears(20));
        assertFalse(userController.isValid(user));
    }

    @Test
    public void shouldNotValidateWhenLoginContainsSpace() {
        User user = new User("heman@mail.ru", "heman@ mail.ru", "name", LocalDate.now().minusYears(20));
        assertFalse(userController.isValid(user));
    }

    @Test
    public void shouldNotValidateWhenDateIsAfterNow() {
        User user = new User("heman@mail.ru", "heman@mail.ru", "name", LocalDate.now().plusYears(20));
        assertFalse(userController.isValid(user));
    }

    @Test
    public void shouldValidateWhenDateEqualsNow() {
        User user = new User("heman@mail.ru", "heman@mail.ru", "name", LocalDate.now());
        assertTrue(userController.isValid(user));
    }
}
