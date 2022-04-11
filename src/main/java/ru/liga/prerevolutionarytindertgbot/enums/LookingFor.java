package ru.liga.prerevolutionarytindertgbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LookingFor {
    MALES("Сударя"),
    FEMALES("Сударыню"),
    ALL("Всѣхъ");

    private final String value;
}
