package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        User newUser;
        if (isValid(user)) {
            newUser = userService.addUser(user);
            log.info("Пользователь " + user.getName() + " успешно добавлен.");
        } else {
            newUser = null;
        }
        return newUser;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Запрошен список пользователей.");
        return userService.getUsers();
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (isValid(user)) {
            userService.updateUser(user);
            log.info("Пользователь " + user.getName() + " успешно обновлен.");
            return user;
        } else {
            throw new ValidationException("Ошибка при обновлении данных пользователя.");
        }
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Запрошен данные польователя " + userService.findUser(id).getName() + ".");
        return userService.findUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователи " + userService.findUser(id).getName() + " и " +
                userService.findUser(friendId).getName() + " теперь друзья.");
        return userService.findUser(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        log.info("Пользователи " + userService.findUser(id).getName() + " и " +
                userService.findUser(friendId).getName() + " больше не друзья.");
        return userService.findUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Запрошен список друзей пользователя " + userService.findUser(id).getName() + ".");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Запрошен список общих друзей пользователей " + userService.findUser(id).getName() + " и " +
                userService.findUser(otherId).getName() + ".");
        return userService.getMutualFriends(id, otherId);
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
