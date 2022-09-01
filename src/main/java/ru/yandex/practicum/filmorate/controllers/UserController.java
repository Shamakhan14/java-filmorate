package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        if (userService.isValid(user)) {
            userService.addUser(user);
            log.info("Пользователь " + user.getName() + " успешно добавлен.");
        }
        return user;
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        log.info("Запрошен список пользователей.");
        return userService.getUsers();
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        boolean contains = false;
        for (User user1: userService.getUsers()) {
            if (user1.getId() == user.getId()) {
                contains = true;
                break;
            }
        }
        if (contains && userService.isValid(user)) {
            userService.updateUser(user);
            log.info("Пользователь " + user.getName() + " успешно обновлен.");
            return user;
        } else {
            throw new ObjectNotFoundException("Ошибка при обновлении данных пользователя.");
        }
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public User getUser(@PathVariable int id) {
        if (userService.findUser(id) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        return userService.findUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userService.findUser(id) != null && userService.findUser(friendId) != null) {
            userService.addFriend(id, friendId);
            return userService.findUser(id);
        }
        throw new ObjectNotFoundException("Неверный ID пользователя.");
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userService.findUser(id) == null || userService.findUser(friendId) == null) {
            throw new ObjectNotFoundException("Неверный ID пользователя.");
        }
        if (!userService.findUser(id).getFriends().contains(friendId)) {
            throw new ObjectNotFoundException("Данные пользовтаели не являются друзьями.");
        }
        userService.removeFriend(id, friendId);
        return userService.findUser(id);
    }

    @GetMapping("/users/{id}/friends")
    @ResponseBody
    public List<User> getFriends(@PathVariable int id) {
        if (userService.findUser(id) == null) throw new ObjectNotFoundException("Неверный ID пользователя.");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        if (userService.findUser(id) == null || userService.findUser(otherId) == null) {
            throw new ObjectNotFoundException("Неверный ID пользователя.");
        }
        return userService.getMutualFriends(id, otherId);
    }
}
