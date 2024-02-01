package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmControllerTest {
    private FilmStorage filmStorage;
    private FilmService filmService;
    private Film film1, film2, film3, film4, film5, film1Updated;

    @Autowired
    public FilmControllerTest(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        film1 = createFilm(0, "name1", "description1", LocalDate.of(2001, 6, 17), 91);
        film2 = createFilm(0, "name2", "description2", LocalDate.of(2002, 6, 17), 92);
        film3 = createFilm(0, "name3", "description3", LocalDate.of(2003, 6, 17), 93);
        film4 = createFilm(0, "name4", "description4", LocalDate.of(2004, 6, 17), 94);
        film5 = createFilm(0, "name5", "description5", LocalDate.of(2005, 6, 17), 95);
        film1Updated = createFilm(1, "name1 updated", "description1 updated", LocalDate.of(2001, 6, 17), 99);
    }


    private Film createFilm(int id, String name, String description, LocalDate releaseDate, int duration) {
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new HashSet<>())
                .build();
    }

    @Test
    public void getAllFilms() {
        film1.setId(1);
        film2.setId(2);
        film3.setId(3);
        film4.setId(4);
        film5.setId(5);
        filmService.createFilm(film1);
        filmService.createFilm(film2);
        filmService.createFilm(film3);
        filmService.createFilm(film4);
        filmService.createFilm(film5);
        assertEquals(5, filmService.getFilms().size());
    }
}