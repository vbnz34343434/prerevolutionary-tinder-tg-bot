package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.EDIT_PROFILE_INFO;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageAskUserInfo;

@Component
@RequiredArgsConstructor
public class ChangeProfileInfoHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;

    @Override
    public String getName() {
        return "Изменить инфо о себе";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        userBotState.setUserBotState(userId, EDIT_PROFILE_INFO);
        return sendMessageMakerService.createTextMessage(chatId, messageAskUserInfo);
    }
}
