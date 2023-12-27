package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> getAllDirectors();

    Director getDirectorById(int directorId);

    Director postDirector(Director director);

    Director putDirector(Director director);

    void deleteDirectorById(int directorId);

    void setFilmDirectorsToDb(Integer filmId, Set<Director> directorSet);

    Set<Director> getFilmDirectorsFromDb(Integer filmId);

    List<Film> setDirectorsToFilmList(List<Film> filmList);

}
