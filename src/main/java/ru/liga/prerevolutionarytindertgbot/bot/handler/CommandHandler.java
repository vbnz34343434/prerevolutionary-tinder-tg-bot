package ru.liga.prerevolutionarytindertgbot.bot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface CommandHandler {

    String getName();

    BotApiMethod<?> handle(long userId, String chatId, String commandText);
}
