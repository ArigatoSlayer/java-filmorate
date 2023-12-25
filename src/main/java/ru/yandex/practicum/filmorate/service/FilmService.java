package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final LocalDate dateStart = LocalDate.of(1895, 12, 28);
    private final int maxChar = 200;

    private final FilmStorage storage;

    public Film updateFilm(Film film) {
        if (!isValidFilm(film)) {
            throw new RuntimeException("Введены неверные данные");
        }
        return storage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        if (!isValidFilm(film)) {
            throw new RuntimeException("Введены неверные данные");
        }
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

    private boolean isValidFilm(Film film) {
        film.setName(film.getName().trim());
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > maxChar) {
            throw new ValidationException("максимальная длина описания — " + maxChar + " символов");
        } else if (film.getReleaseDate().isBefore(dateStart)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
