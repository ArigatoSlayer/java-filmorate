package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Builder
public class Film {

    private int id;

    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200, message = "Description field must be less 200 characters!")
    private String description;

    @ReleaseDateValidation(message = "Release date is before 28.12.1895!")
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private int duration;

    private Mpa mpa;
    private List<Genre> genres;
    private Set<Integer> likes = new HashSet<>();
    private Set<Director> directors;

    public void addDirectorToDirectorsSet(Director director) {
        this.directors.add(director);
    }
}