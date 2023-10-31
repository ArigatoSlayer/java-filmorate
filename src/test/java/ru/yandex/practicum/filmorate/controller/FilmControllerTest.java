package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    void setController() {
        controller = new FilmController();
    }

    @Test
    void getFilmsTest() {
        Film film = new Film(0, "as", "asd", LocalDate.of(2022, 12, 12), 40);
        controller.createFilm(film);
        controller.createFilm(film);
        controller.createFilm(film);
        Assertions.assertEquals(3, controller.getFilms().size());
    }

    @Test
    void createFilm() {
        Film film = new Film(0, "aaa", "asd", LocalDate.of(2022, 12, 12), 40);
        controller.createFilm(film);
        Assertions.assertEquals(film.hashCode(), controller.getFilms().get(1).hashCode());
    }

    @Test
    void createFilmIfNameEmpty() {
        Film film = new Film(0, "  ", "asd", LocalDate.of(2022, 12, 12), 40);
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfDescriptionOver200() {
        Film film = new Film(0, "asd",
                new String(new char[200]).replace("", " "),
                LocalDate.of(2022, 12, 12), 40);
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfBefore1875() {
        Film film = new Film(0, "as", "asd", LocalDate.of(1001, 12, 12), 40);
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfDurationUp0() {
        Film film = new Film(0, "as", "asd", LocalDate.of(2022, 12, 12), 0);
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void updateFilmTest() {
        Film film = new Film(0, "as", "asd", LocalDate.of(2022, 12, 12), 40);
        controller.createFilm(film);
        Film filmId1 = new Film(1, "nas", "das", LocalDate.of(2022, 12, 12), 30);
        controller.updateFilm(filmId1);
        Assertions.assertEquals("nas", controller.getFilms().get(1).getName());
        Assertions.assertEquals("das", controller.getFilms().get(1).getDescription());
        Assertions.assertEquals(30, controller.getFilms().get(1).getDuration());
    }

}
