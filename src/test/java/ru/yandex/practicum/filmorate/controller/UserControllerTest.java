package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

class UserControllerTest {
    private UserController controller;
    private UserStorage storage;
    private UserService service;

    @BeforeEach
    void setController() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);
        controller = new UserController(service);
    }

    @Test
    void getAllUsers() {
        User user = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        User user1 = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        User user2 = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        controller.createUser(user);
        controller.createUser(user1);
        controller.createUser(user2);
        Assertions.assertEquals(3, controller.getAllUsers().size());
    }

    @Test
    void createUser() {
        User user = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        User user1 = User.builder().id(0).birthday(LocalDate.of(2030, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        User user2 = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("").name("qwe").build();
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user1));
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user2));
    }

    @Test
    void updateUser() {
        User user = User.builder().id(0).birthday(LocalDate.of(2000, 11, 10)).email("email@mail.ru")
                .friends(new HashSet<>()).login("lol").name("qwe").build();
        controller.createUser(user);
        controller.updateUser(user);
        Assertions.assertEquals(user.hashCode(), controller.getAllUsers().get(0).hashCode());
    }
}