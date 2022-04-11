package ru.liga.prerevolutionarytindertgbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("Сударъ"),
    FEMALE("Сударыня");

    private final String value;
}