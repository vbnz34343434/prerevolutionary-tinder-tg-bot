package ru.liga.prerevolutionarytindertgbot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@Slf4j
public class MessageHandler {
    private final CommandContext commandContext;

    public MessageHandler(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public BotApiMethod<?> answerMessage(Message message) {
        long userId = message.getFrom().getId();
        String chatId = message.getChatId().toString();
        String request = message.getText();
        return commandContext.handle(userId, chatId, request);
    }

}
