package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

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
        getFilmById(filmId).getLikes().add(userId);
        return getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        if (getFilms().contains(getFilmById(filmId)) && getFilmById(filmId).getLikes().contains(userId)) {
            getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Пользователь с id: " + userId + " или фильм с id: " + filmId + " ! Не найдено");
        }
        return getFilmById(filmId);
    }

    public List<Film> topFilms(int count) {
        return storage.getFilms().stream().sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
