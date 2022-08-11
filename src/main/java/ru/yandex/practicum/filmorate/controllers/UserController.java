package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private static int ids = 0;

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        if (isValid(user)) {
            user.setId(++ids);
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getName() + " успешно добавлен.");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Запрошен список пользователей.");
        return new ArrayList<>(users.values());
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId()) && isValid(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getName() + " успешно обновлен.");
            return user;
        } else {
            throw new ValidationException("Ошибка при обновлении данных пользователя.");
        }
    }

    private boolean isValid(User user) {
        if (user.getName().isBlank()) {
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
