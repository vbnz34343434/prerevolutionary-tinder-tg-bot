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
import ru.liga.prerevolutionarytindertgbot.service.UserService;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.*;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.*;

@Component
public class StateHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;
    private final UserDataCacheProxy userDataCacheProxy;
    private final UserService userService;
    private final TelegramTinderBot bot;

    public StateHandler(UserBotState userBotState, SendMessageMakerService sendMessageMakerService, ReplyKeyboardMakerService replyKeyboardMakerService, UserDataCacheProxy userDataCacheProxy, UserService userService, @Lazy TelegramTinderBot bot) {
        this.userBotState = userBotState;
        this.sendMessageMakerService = sendMessageMakerService;
        this.replyKeyboardMakerService = replyKeyboardMakerService;
        this.userDataCacheProxy = userDataCacheProxy;
        this.userService = userService;
        this.bot = bot;
    }

    @Override
    public String getName() {
        return "State";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        User user;
        TelegramBotState state = userBotState.getUserBotState(userId);

        switch (state) {
            case REGISTRATION_NAME:
                userDataCacheProxy.getUserById(userId).setName(commandText);
                userBotState.setUserBotState(userId, REGISTRATION_PROFILE_INFO);
                return sendMessageMakerService.createTextMessage(chatId, messageAskUserInfo);
            case REGISTRATION_PROFILE_INFO:
                user = userDataCacheProxy.getUserById(userId);
                user.setHeader(getHeader(commandText));
                user.setDescription(getDescription(commandText));
                userBotState.setUserBotState(userId, REGISTRATION_LOOKING_FOR);
                return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageAskPreferences, replyKeyboardMakerService.getRegistrationChooseLookingForInlineMenu());
            case EDIT_NAME:
                user = userDataCacheProxy.getUserById(userId);
                user.setName(commandText);
                userService.updateUser(user);
                userBotState.setUserBotState(userId, VIEW_PROFILE);
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                                chatId,
                                user.getGender().getValue().concat(", ").concat(user.getName()),
                                user.getProfileImage(),
                                replyKeyboardMakerService.getChangeProfileMenuKeyboard()));
                break;
            case EDIT_PROFILE_INFO:
                user = userDataCacheProxy.getUserById(userId);
                user.setHeader(getHeader(commandText));
                user.setDescription(getDescription(commandText));
                userService.updateUser(user);
                userBotState.setUserBotState(userId, VIEW_PROFILE);
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                                chatId,
                                user.getGender().getValue().concat(", ").concat(user.getName()),
                                user.getProfileImage(),
                                replyKeyboardMakerService.getChangeProfileMenuKeyboard()));
                break;
        }
        return null;
    }

    private String getHeader(String commandText) {
        String[] lines = commandText.split("\n");
        return lines.length > 1 ? lines[0].trim() : commandText.split(" ")[0].trim();
    }

    private String getDescription(String commandText) {
        String[] lines = commandText.split("\n");
        return lines.length > 1 ? commandText.substring(lines[0].length()).trim() : commandText.substring(commandText.split(" ")[0].length()).trim();
    }
}
