package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.REGISTRATION_GENDER;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.*;

@Component
@RequiredArgsConstructor
public class StartHandler implements CommandHandler {

    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;

    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {

        if (userBotState.isUserRegistered(userId)) {
            return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageAlreadyRegistered, replyKeyboardMakerService.getMainMenuKeyboard());
        }
        userBotState.setUserBotState(userId, REGISTRATION_GENDER);
        return sendMessageMakerService.createTextMessageWithReplyKeyboard(
                chatId,
                messageHello + "\n" + messageAskGender,
                replyKeyboardMakerService.getRegistrationChooseGenderInlineMenu()
        );
    }
}
