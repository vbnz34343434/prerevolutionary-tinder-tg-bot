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
import ru.liga.prerevolutionarytindertgbot.service.SearchPairService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageAskGender;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageNotRegistered;

@Component
public class SearchHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SendMessageMakerService sendMessageMakerService;
    private final SearchPairService searchPairService;
    private final TelegramTinderBot bot;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;

    public SearchHandler(UserBotState userBotState, SendMessageMakerService sendMessageMakerService, SearchPairService searchPairService, @Lazy TelegramTinderBot bot, ReplyKeyboardMakerService replyKeyboardMakerService) {
        this.userBotState = userBotState;
        this.sendMessageMakerService = sendMessageMakerService;
        this.searchPairService = searchPairService;
        this.bot = bot;
        this.replyKeyboardMakerService = replyKeyboardMakerService;
    }

    @Override
    public String getName() {
        return "Поиск";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        if (!userBotState.isUserRegistered(userId)) {
            return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageNotRegistered + "\n" + messageAskGender, replyKeyboardMakerService.getRegistrationChooseGenderInlineMenu());
        }
        userBotState.setUserBotState(userId, TelegramBotState.IN_SEARCH);
        User user = searchPairService.searchNext(userId, false);
        bot.executeMessage(
                sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                        chatId,
                        user.getGender().getValue() + ", " + user.getName(),
                        user.getProfileImage(),
                        replyKeyboardMakerService.getNavigationMenuKeyboard())
        );
        return null;
    }
}
