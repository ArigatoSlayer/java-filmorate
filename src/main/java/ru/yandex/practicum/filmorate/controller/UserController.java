package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping("/users")
    public Map<Integer, User> getAllUsers() {
        log.trace("method: Get. Model: User");
        return users;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("method: Post. Model: User." + user.getLogin());
        if (isValidUser(user)) {
            user.setId(id);
            id++;
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("UserId: " + user.getId() + " added");
        }
        return users.get(user.getId());
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("UserId: " + user.getId() + " update");
            return users.get(user.getId());
        } else {
            return createUser(user);
        }
    }

    private boolean isValidUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email пустой или не содержит: @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин пустой или содержит пробел");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения установлена в будущем");
        }
        return true;
    }
}
