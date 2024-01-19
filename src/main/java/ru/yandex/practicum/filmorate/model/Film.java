package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
public class Film {

    private int id;

    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200, message = "Описание фильма должно не более 200 символов.")
    private String description;

    @ReleaseDateValidation(message = "Дата выхода фильма должна быть не ранее 28.12.1895.")
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private int duration;

    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Director> directors;

    public void addDirectorToDirectorsSet(Director director) {
        this.directors.add(director);
    }
}