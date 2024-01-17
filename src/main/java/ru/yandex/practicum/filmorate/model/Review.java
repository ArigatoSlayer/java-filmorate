package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Review {

    Integer reviewId;

    @NotBlank
    @NotNull
    String content;

    @NotNull
    Boolean isPositive;

    @NotNull
    Integer userId;

    @NotNull
    Integer filmId;

    @NotNull
    Integer useful;

}
