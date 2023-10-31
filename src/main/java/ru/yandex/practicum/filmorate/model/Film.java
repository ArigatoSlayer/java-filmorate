package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotEmpty(message = "Поле не может быть пустым")
    private String name;
    @NotEmpty(message = "Поле не может быть пустым")
    @Size(max = 200, message = "Описание не может быть, длиннее 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Поле не может иметь отрицательное значение")
    private int duration;
}