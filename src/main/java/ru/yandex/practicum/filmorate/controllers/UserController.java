package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private UserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        userStorage = inMemoryUserStorage;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        if (isValid(user)) {
            userStorage.addUser(user);
            log.info("Пользователь " + user.getName() + " успешно добавлен.");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Запрошен список пользователей.");
        return userStorage.getUsers();
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        boolean contains = false;
        for (User user1: userStorage.getUsers()) {
            if (user1.getId() == user.getId()) {
                contains = true;
                break;
            }
        }
        if (contains && isValid(user)) {
            userStorage.updateUser(user);
            log.info("Пользователь " + user.getName() + " успешно обновлен.");
            return user;
        } else {
            throw new ValidationException("Ошибка при обновлении данных пользователя.");
        }
    }

    private boolean isValid(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ") ||
                user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверно введены данные пользователя.");
        }
        return true;
    }
}
