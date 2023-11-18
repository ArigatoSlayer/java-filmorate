package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashSet;

class FilmControllerTest {
    private FilmController controller;
    private FilmService service;
    private FilmStorage storage;

    @BeforeEach
    void setController() {
        storage = new InMemoryFilmStorage();
        service = new FilmService(storage);
        controller = new FilmController(service);
    }

    @Test
    void getFilmsTest() {
        Film film = Film.builder().name("asd").releaseDate(LocalDate.of(2022, 11, 10))
                .likes(new HashSet<>()).duration(40).description("qwe").id(0).build();
        controller.createFilm(film);
        controller.createFilm(film);
        controller.createFilm(film);
        Assertions.assertEquals(3, controller.getFilms().size());
    }

    @Test
    void createFilm() {
        Film film = Film.builder().name("asd").likes(new HashSet<>()).
                duration(40).description("qwe").id(0).releaseDate(LocalDate.of(2022, 11, 10)).build();
        controller.createFilm(film);
        Assertions.assertEquals(film.hashCode(), controller.getFilms().get(0).hashCode());
    }

    @Test
    void createFilmIfNameEmpty() {
        Film film = Film.builder().name(" ").likes(new HashSet<>())
                .releaseDate(LocalDate.of(2022, 11, 10)).duration(40).description("qwe").id(0).build();
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfDescriptionOver200() {
        Film film = Film.builder().name("asd").likes(new HashSet<>()).releaseDate(LocalDate.of(2022, 11, 10)).
                duration(40).description(new String(new char[200]).replace("", " ")).id(0).build();
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfBefore1875() {
        Film film = Film.builder().name(" ").likes(new HashSet<>())
                .releaseDate(LocalDate.of(1070, 11, 10)).duration(40).description("qwe").id(0).build();
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void createFilmIfDurationUp0() {
        Film film = Film.builder().name(" ").likes(new HashSet<>())
                .releaseDate(LocalDate.of(2022, 11, 10)).duration(0).description("qwe").id(0).build();
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void updateFilmTest() {
        Film film = Film.builder().name("q").likes(new HashSet<>())
                .releaseDate(LocalDate.of(2022, 11, 10)).duration(40).description("qwe").id(0).build();
        controller.createFilm(film);
        Film filmId1 = Film.builder().name("q").likes(new HashSet<>())
                .releaseDate(LocalDate.of(2022, 11, 10)).duration(40).description("qwe").id(1).build();
        controller.updateFilm(filmId1);
        Assertions.assertEquals(filmId1.hashCode(), controller.getFilms().get(0).hashCode());
    }

}
