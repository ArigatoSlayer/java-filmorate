package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

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

    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Login field should be without whitespaces!")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent(message = "Birthday field must contain a past date!")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

}