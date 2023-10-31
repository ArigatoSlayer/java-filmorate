package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    UserController controller;

    @BeforeEach
    void setController() {
        controller = new UserController();
    }

    @Test
    void getAllUsers() {
        User user = new User(0, "email@mail.ru", "lol", "qwe", LocalDate.of(2022, 12, 3));
        User user1 = new User(0, "email@mail.ru", "lol", "qwe", LocalDate.of(2022, 12, 3));
        User user2 = new User(0, "email@mail.ru", "lol", "qwe", LocalDate.of(2022, 12, 3));
        controller.createUser(user);
        controller.createUser(user1);
        controller.createUser(user2);
        Assertions.assertEquals(3, controller.getAllUsers().size());
    }

    @Test
    void createUser() {
        User user = new User(0, "mail.ru", "lol", "qwe", LocalDate.of(2022, 12, 3));
        User user1 = new User(0, "mail.ru", "lol", "qwe", LocalDate.of(2030, 12, 3));
        User user2 = new User(0, "mail.ru", "  ", "qwe", LocalDate.of(2022, 12, 3));
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user1));
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user2));
    }

    @Test
    void updateUser() {
        User user = new User(0, "mail@mail.ru", "lol", "qwe", LocalDate.of(2022, 12, 3));
        controller.createUser(user);
        controller.updateUser(user);
        Assertions.assertEquals(user.hashCode(), controller.getAllUsers().get(0).hashCode());
    }
}