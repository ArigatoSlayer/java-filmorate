package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {
    private final UserStorage userStorage;
    private final UserService userService;
    private final User user1, user2, user3, user4, user5, user1Updated;

    @Autowired
    public UserControllerTest(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
        user1 = createUser(0, "login1", "name1", "user1@ya.ru", LocalDate.of(1999, 2, 1));
        user2 = createUser(0, "login2", "name2", "user2@ya.ru", LocalDate.of(1999, 2, 2));
        user3 = createUser(0, "login3", "name3", "user3@ya.ru", LocalDate.of(1999, 2, 3));
        user4 = createUser(0, "login4", "name4", "user4@ya.ru", LocalDate.of(1999, 2, 4));
        user5 = createUser(0, "login5", "name5", "user5@ya.ru", LocalDate.of(1999, 2, 5));
        user1Updated = createUser(1, "login1", "Updated name1", "new-email@yandex.ru", LocalDate.of(1999, 2, 1));
    }

    private User createUser(int id, String login, String name, String email, LocalDate birthday) {
        return User.builder()
                .id(id)
                .login(login)
                .name(name)
                .email(email)
                .birthday(birthday)
                .friends(new HashSet<>())
                .build();
    }

    @Test
    public void getAllUserTest() {
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        user4.setId(4);
        user5.setId(5);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);
        assertEquals(5, userStorage.getAllUsers().size());
    }

    @Test
    public void updateUserTest() {
        user1.setId(1);
        userService.createUser(user1);
        userService.updateUser(user1Updated);
        assertEquals(userService.getUserById(1), user1Updated);
    }
}