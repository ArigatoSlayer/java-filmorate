package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
