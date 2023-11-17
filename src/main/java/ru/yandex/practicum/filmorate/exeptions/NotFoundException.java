package ru.yandex.practicum.filmorate.exeptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String e) {
        super(e);
    }
}
