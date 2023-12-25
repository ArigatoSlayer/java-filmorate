package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    @PositiveOrZero
    private int id;
    @NotNull
    @Size(max = 100, message = "Слишком длинное название жанра. Максимальное количество символов - 100")
    private String name;
}
