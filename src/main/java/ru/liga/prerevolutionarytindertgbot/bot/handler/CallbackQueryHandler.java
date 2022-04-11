package ru.liga.prerevolutionarytindertgbot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramTinderBot;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;
import ru.liga.prerevolutionarytindertgbot.enums.LookingFor;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;
import ru.liga.prerevolutionarytindertgbot.service.UserDataCacheProxy;
import ru.liga.prerevolutionarytindertgbot.service.UserService;

import java.util.Locale;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.*;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.*;

@Component
@Slf4j
public class CallbackQueryHandler {
    private final ReplyKeyboardMakerService replyKeyboardMakerService;
    private final SendMessageMakerService sendMessageMakerService;
    private final UserBotState userBotState;
    private final UserDataCacheProxy userDataCacheProxy;
    private final UserService userService;
    private final TelegramTinderBot bot;

    public CallbackQueryHandler(ReplyKeyboardMakerService replyKeyboardMakerService, SendMessageMakerService sendMessageMakerService, UserBotState userBotState, UserDataCacheProxy userDataCacheProxy, UserService userService, @Lazy TelegramTinderBot bot) {
        this.replyKeyboardMakerService = replyKeyboardMakerService;
        this.sendMessageMakerService = sendMessageMakerService;
        this.userBotState = userBotState;
        this.userDataCacheProxy = userDataCacheProxy;
        this.userService = userService;
        this.bot = bot;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final String chatId = callbackQuery.getMessage().getChatId().toString();
        long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();

        TelegramBotState state;
        BotApiMethod<?> message;
        User user;

        switch (data) {
            case "gender male":
            case "gender female":
                if (userBotState.isUserRegistered(userId)) {
                    return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageAlreadyRegistered, replyKeyboardMakerService.getMainMenuKeyboard());
                }
                state = REGISTRATION_NAME;
                userDataCacheProxy.getUserById(userId).setGender(data.equals("gender male") ? Gender.MALE : Gender.FEMALE);
                message = sendMessageMakerService.createTextMessage(chatId, messageAskName);
                break;
            case "looking for males":
            case "looking for females":
            case "looking for all":
                if (userBotState.isUserRegistered(userId)) {
                    return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageAlreadyRegistered, replyKeyboardMakerService.getMainMenuKeyboard());
                }
                user = userDataCacheProxy.getUserById(userId);
                user.setLookingFor(LookingFor.valueOf(data.substring("looking for ".length()).toUpperCase()));

                log.info("start user registration: {}", user);
                if (!userService.registerUser(user)) {
                    return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageRegistrationError, replyKeyboardMakerService.getRegistrationChooseLookingForInlineMenu());
                }
                message = sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageRegistrationSuccess, replyKeyboardMakerService.getMainMenuKeyboard());
                state = REGISTRATION_COMPLETED;
                break;
            case "change gender to male":
            case "change gender to female":
                user = userDataCacheProxy.getUserById(userId);
                user.setGender(Gender.valueOf(data.substring("change gender to ".length()).toUpperCase()));
                userService.updateUser(user);
                message = null;
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                                chatId,
                                user.getGender().getValue().concat(", ").concat(user.getName()),
                                user.getProfileImage(),
                                replyKeyboardMakerService.getChangeProfileMenuKeyboard()));
                state = VIEW_PROFILE;
                break;
            case "change looking for to males":
            case "change looking for to females":
            case "change looking for to all":
                user = userDataCacheProxy.getUserById(userId);
                user.setLookingFor(LookingFor.valueOf(data.substring("change looking for to ".length()).toUpperCase()));
                userService.updateUser(user);
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                                chatId,
                                user.getGender().getValue().concat(", ").concat(user.getName()),
                                user.getProfileImage(),
                                replyKeyboardMakerService.getChangeProfileMenuKeyboard()));
                state = VIEW_PROFILE;
                message = null;
                break;
            default:
                return sendMessageMakerService.createTextMessage(chatId, messageUnknownCommand);
        }

        userBotState.setUserBotState(userId, state);
        return message;
    }
}
