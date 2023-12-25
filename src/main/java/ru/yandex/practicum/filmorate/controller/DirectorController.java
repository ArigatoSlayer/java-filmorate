package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable int directorId) {
        return directorService.getDirectorById(directorId);
    }

    @PostMapping
    public Director postDirector(@Valid @RequestBody Director director) {
        return directorService.postDirector(director);
    }

    @PutMapping
    public Director putDirector(@Valid @RequestBody Director director) {
        return directorService.putDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable int directorId) {
        directorService.deleteDirectorById(directorId);
    }

}
