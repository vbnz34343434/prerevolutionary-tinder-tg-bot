package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.EDIT_NAME;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageAskName;

@Component
@RequiredArgsConstructor
public class ChangeNameHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;

    @Override
    public String getName() {
        return "Изменить имя";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        userBotState.setUserBotState(userId, EDIT_NAME);
        return sendMessageMakerService.createTextMessage(chatId, messageAskName);
    }
}
