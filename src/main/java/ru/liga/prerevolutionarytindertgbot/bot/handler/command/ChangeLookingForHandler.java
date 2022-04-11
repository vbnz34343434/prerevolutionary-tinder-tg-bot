package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.EDIT_LOOKING_FOR;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageAskPreferences;

@Component
@RequiredArgsConstructor
public class ChangeLookingForHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;

    @Override
    public String getName() {
        return "Изменить кого ищете";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        userBotState.setUserBotState(userId, EDIT_LOOKING_FOR);
        return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageAskPreferences, replyKeyboardMakerService.getEditLookingForInlineMenu());
    }
}
