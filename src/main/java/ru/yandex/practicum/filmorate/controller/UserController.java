package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.trace("method: Get. Model: User");
        return service.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("method: Post. Model: User." + user.getLogin());
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("method: Put. Model: User." + user.getLogin());
        return service.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addToFriendList(@PathVariable int userId, @PathVariable int friendId) {
        return service.addToFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        return service.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/")
    public List<User> getAllFriends(@PathVariable int userId) {
        return service.getAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> mutalFriend(@PathVariable int userId, @PathVariable int friendId) {
        return service.getMutualFriends(userId, friendId);
    }

}