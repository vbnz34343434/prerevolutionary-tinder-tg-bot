package ru.liga.prerevolutionarytindertgbot.bot;

public enum TelegramBotState {
    UNKNOWN_USER,
    REGISTRATION_GENDER,
    REGISTRATION_NAME,
    REGISTRATION_PROFILE_INFO,
    REGISTRATION_LOOKING_FOR,
    REGISTRATION_COMPLETED,
    VIEW_PROFILE,
    IN_SEARCH,
    VIEW_PAIRS,
    EDIT_NAME,
    EDIT_GENDER,
    EDIT_PROFILE_INFO,
    EDIT_LOOKING_FOR
}
