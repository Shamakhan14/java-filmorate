package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDBStorageTests {

    private final UserDbStorage userStorage;

    @Test
    public void test() {
        //creating
        User user = new User("email@email.com", "login", "name", LocalDate.now());
        User user2 = userStorage.addUser(user);
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getLogin(), user2.getLogin());

        //finding
        User user3 = userStorage.findUser(user2.getId());
        assertEquals(user2.getId(), user3.getId());
        assertEquals(user2.getName(), user3.getName());
        assertEquals(user2.getEmail(), user3.getEmail());
        assertEquals(user2.getLogin(), user3.getLogin());

        //updating
        user2.setName("name2");
        userStorage.updateUser(user2);
        User user4 = userStorage.findUser(user2.getId());
        assertEquals(user4.getId(), user2.getId());
        assertEquals("name2", user2.getName());
        assertEquals(user4.getEmail(), user2.getEmail());
        assertEquals(user4.getLogin(), user2.getLogin());

        //getting all
        assertEquals(1, userStorage.getUsers().size());

        //adding friend
        User user5 = new User("3email@email.com", "login3", "name3", LocalDate.now());
        User user6 = userStorage.addUser(user3);
        userStorage.addFriend(user2.getId(), user6.getId());
        assertEquals(1, userStorage.getFriends(user2.getId()).size());

        //removing friend
        userStorage.removeFriend(user2.getId(), user6.getId());
        assertEquals(0, userStorage.getFriends(user2.getId()).size());
    }
}