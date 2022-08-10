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

    public boolean isValid(User user) {
        boolean isValid = true;
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") ||
            user.getLogin().isEmpty() || user.getLogin().contains(" ") ||
            user.getBirthday().isAfter(LocalDate.now())) {
            isValid = false;
        }
        return isValid;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        if (isValid(user)) {
            if (user.getId() == 0) {
                user.setId(++ids);
            }
            users.put(user.getId(), user);
            log.info("Пользователь " + user.getName() + " успешно добавлен.");
        } else {
            throw new ValidationException("Неверно введены данные пользователя.");
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
        } else {
            throw new ValidationException("Неверно введены данные о фильме.");
        }
        return user;
    }
}
