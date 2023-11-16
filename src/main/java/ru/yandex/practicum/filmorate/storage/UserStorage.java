package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User createUser(@Valid @RequestBody User user);

    User updateUser(@Valid @RequestBody User user);
}
