package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final LocalDate dateStart = LocalDate.of(1895, 12, 28);
    private final int maxChar = 200;

    private final FilmStorage filmStorage;
    private final DirectorStorage directorStorage;

    public Film updateFilm(Film film) {
        if (!isValidFilm(film)) {
            throw new RuntimeException("Введены неверные данные");
        }
        directorStorage.setFilmDirectorsToDb(film.getId(), film.getDirectors());
        Film resultFilm = filmStorage.updateFilm(film);
        resultFilm.setDirectors(directorStorage.getFilmDirectorsFromDb(film.getId()));
        return resultFilm;
    }

    public Film createFilm(Film film) {
        if (!isValidFilm(film)) {
            throw new RuntimeException("Введены неверные данные");
        }
        Film resultFilm = filmStorage.createFilm(film);
        directorStorage.setFilmDirectorsToDb(resultFilm.getId(), film.getDirectors());
        resultFilm.setDirectors(directorStorage.getFilmDirectorsFromDb(film.getId()));
        return resultFilm;
    }

    public List<Film> getFilms() {
        return directorStorage.setDirectorsToFilmList(filmStorage.getFilms());
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public List<Film> getAllDirectorFilms(int directorId, String sortBy) {
        directorStorage.getDirectorById(directorId);
        if ("likes".equals(sortBy)) {
            return directorStorage.setDirectorsToFilmList(filmStorage.getAllDirectorFilmsOrderByLikes(directorId));
        }
        if ("year".equals(sortBy)) {
            return directorStorage.setDirectorsToFilmList(filmStorage.getAllDirectorFilmsOrderByYear(directorId));
        }
        throw new RuntimeException("Введены неверные данные");
    }

    public Film putLike(int filmId, int userId) {
        Film film = filmStorage.addLike(filmId, userId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.deleteLike(filmId, userId);
        film.setDirectors(directorStorage.getFilmDirectorsFromDb(filmId));
        return film;
    }

    public List<Film> topFilms(int count) {
        return directorStorage.setDirectorsToFilmList(filmStorage.getListOfTopFilms(count));
    }

    public List<Film> searchBySubstring(String str, List<String> by) {
        List<Film> films;
        if (by.size() == 2) {
            films = filmStorage.searchBySubstring(str);
        } else if (by.size() == 1 && by.contains("title")) {
            films = filmStorage.searchBySubstringByFilms(str);
        } else if (by.size() == 1 && by.contains("director")) {
            films = filmStorage.searchBySubstringByDirectors(str);
        } else {
            throw new NotFoundException("Фильмы с подстрокой " + str + " не найдены");
        }
        return directorStorage.setDirectorsToFilmList(films);
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
