package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film);

    Film createFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(int id);
}
