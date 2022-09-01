package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{

    private Map<Integer, User> users;
    private static int ids = 0;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public void addUser(User user) {
        user.setId(++ids);
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findUser(Integer userID) {
        return users.get(userID);
    }
}
