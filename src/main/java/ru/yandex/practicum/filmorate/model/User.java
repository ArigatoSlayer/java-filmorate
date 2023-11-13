package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Поле не может быть пустым")
    private LocalDate birthday;
}