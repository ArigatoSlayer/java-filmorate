package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Mpa {
    @PositiveOrZero
    private int id;
    @NotNull
    @Size(max = 3, message = "Слишком длинное имя. Максимальное количество символов - 10")
    private String name;
}
