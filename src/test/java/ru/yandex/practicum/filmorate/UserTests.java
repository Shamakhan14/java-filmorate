package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new UserDbStorage(new JdbcTemplate());
    }

    @Test
    public void createUserTest() {
        User user = new User("email@email.com", "login", "name", LocalDate.now());
        User newUser = userStorage.addUser(user);
        assertEquals(1, newUser.getId());
        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getEmail(), newUser.getEmail());
        assertEquals(user.getLogin(), newUser.getLogin());
    }
}
