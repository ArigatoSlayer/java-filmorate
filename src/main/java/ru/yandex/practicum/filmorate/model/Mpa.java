package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
