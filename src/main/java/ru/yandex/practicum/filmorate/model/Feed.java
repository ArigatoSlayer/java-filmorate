package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Feed {

    @NotNull
    long timestamp;

    @NotNull
    Integer userId;

    @NotBlank
    String eventType;

    @NotBlank
    String operation;

    @NotNull
    Integer eventId;

    @NotNull
    Integer entityId;
}