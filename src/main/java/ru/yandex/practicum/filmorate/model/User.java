package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotEmpty(message = "Поле не может быть пустым")
    @Email(message = "Некорректный формат адреса электронной почты")
    private String email;
    @NonNull
    @NotEmpty(message = "Поле не может быть пустым")
    private String login;
    private String name;
//    @Past(message = "Дата рождение написано в будущем времени")
    @NotEmpty(message = "Поле не может быть пустым")
    private LocalDate birthday;
}