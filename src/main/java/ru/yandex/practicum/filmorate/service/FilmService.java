package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage storage;

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film getFilmById(int id) {
        return storage.getFilmById(id);
    }

    public Film putLike(int filmId, int userId) {
        return storage.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) {
        return storage.deleteLike(filmId, userId);
    }

    public List<Film> topFilms(int count) {
        return storage.getListOfTopFilms(count);
    }
}
