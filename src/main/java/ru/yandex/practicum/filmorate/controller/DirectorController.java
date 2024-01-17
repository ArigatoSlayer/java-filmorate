package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/directors")
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/directors/{id}")
    public Director getDirectorById(@PathVariable int id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping("/directors")
    public Director postDirector(@Valid @RequestBody Director director) {
        return directorService.postDirector(director);
    }

    @PutMapping("/directors")
    public Director putDirector(@Valid @RequestBody Director director) {
        return directorService.putDirector(director);
    }

    @DeleteMapping("/directors/{id}")
    public void deleteDirectorById(@PathVariable int id) {
        directorService.deleteDirectorById(id);
    }

}

