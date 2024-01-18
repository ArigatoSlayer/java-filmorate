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

    @NotBlank
    @Size(max = 200)
    private String content;

    @NotNull
    private Boolean isPositive;

    private Integer userId;

    private Integer filmId;

    private Integer useful;

}
