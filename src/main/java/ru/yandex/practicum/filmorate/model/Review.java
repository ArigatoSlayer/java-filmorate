package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Review {

    Integer reviewId;

    @NotBlank(message = "Отзыв должен содержать контент.")
    @Size(max = 200, message = "Контент отзыва дожен быть не более 200 символов.")
    private String content;

    @NotNull(message = "Отзыв должен содержать оценку.")
    private Boolean isPositive;

    @NotNull(message = "Отзыв должен содержать идентификатоор пользователя.")
    private Integer userId;

    @NotNull(message = "Отзыв должен содержать идентификатор фильма.")
    private Integer filmId;

    private Integer useful;

}
