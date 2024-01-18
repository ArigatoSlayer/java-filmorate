package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private Integer id;

    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелов.")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent(message = "Дата рождения не должна быть в прошлом.")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

}