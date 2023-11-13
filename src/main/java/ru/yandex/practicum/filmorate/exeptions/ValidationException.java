package ru.yandex.practicum.filmorate.exeptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String mes) {
        super(mes);
    }
}
