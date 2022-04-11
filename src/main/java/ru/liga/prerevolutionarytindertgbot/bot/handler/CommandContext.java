package ru.liga.prerevolutionarytindertgbot.bot.handler;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandContext {
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public CommandContext(List<CommandHandler> commandHandlersList) {
        commandHandlersList.forEach(handler -> commandHandlers.put(handler.getName(), handler));
    }

    public BotApiMethod<?> handle(long userId, String chatId, String commandName) {
        return getCommandHandler(commandName).handle(userId, chatId, commandName);
    }

    private CommandHandler getCommandHandler(String commandName) {
        return commandHandlers.getOrDefault(commandName, commandHandlers.get("State"));
    }
}
