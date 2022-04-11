package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramTinderBot;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;
import ru.liga.prerevolutionarytindertgbot.service.UserDataCacheProxy;

import static ru.liga.prerevolutionarytindertgbot.util.Constant.*;

@Component
public class ShowProfileHandler implements CommandHandler {

    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;
    private final TelegramTinderBot bot;
    private final UserDataCacheProxy userDataCacheProxy;

    public ShowProfileHandler(UserBotState userBotState,
                              SendMessageMakerService sendMessageMakerService,
                              ReplyKeyboardMakerService replyKeyboardMakerService,
                              @Lazy TelegramTinderBot bot, UserDataCacheProxy userDataCacheProxy) {
        this.userBotState = userBotState;
        this.sendMessageMakerService = sendMessageMakerService;
        this.replyKeyboardMakerService = replyKeyboardMakerService;
        this.bot = bot;
        this.userDataCacheProxy = userDataCacheProxy;
    }

    @Override
    public String getName() {
        return "Анкета";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        if (!userBotState.isUserRegistered(userId)) {
            return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageNotRegistered + "\n" + messageAskGender, replyKeyboardMakerService.getRegistrationChooseGenderInlineMenu());
        }
        User user = userDataCacheProxy.getUserById(userId);
        userBotState.setUserBotState(userId, TelegramBotState.VIEW_PROFILE);
        bot.executeMessage(
                sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                        chatId,
                        user.getGender().getValue().concat(", ").concat(user.getName()),
                        user.getProfileImage(),
                        replyKeyboardMakerService.getChangeProfileMenuKeyboard()));
        return null;
    }
}
