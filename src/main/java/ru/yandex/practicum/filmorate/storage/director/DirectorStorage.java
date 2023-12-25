package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> getAllDirectors();

    Director getDirectorById(int directorId);

    Director postDirector(Director director);

    Director putDirector(Director director);

    void deleteDirectorById(int directorId);

}
