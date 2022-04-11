package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramTinderBot;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.cache.UserBotState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.service.PairsNavigationService;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageAskGender;
import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageNotRegistered;

@Component
public class ShowFavouritesHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;
    private final SendMessageMakerService sendMessageMakerService;
    private final PairsNavigationService pairsNavigationService;
    private final TelegramTinderBot bot;

    public ShowFavouritesHandler(UserBotState userBotState, ReplyKeyboardMakerService replyKeyboardMakerService, SendMessageMakerService sendMessageMakerService, PairsNavigationService pairsNavigationService, @Lazy TelegramTinderBot bot) {
        this.userBotState = userBotState;
        this.replyKeyboardMakerService = replyKeyboardMakerService;
        this.sendMessageMakerService = sendMessageMakerService;
        this.pairsNavigationService = pairsNavigationService;
        this.bot = bot;
    }

    @Override
    public String getName() {
        return "Любимцы";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        if (!userBotState.isUserRegistered(userId)) {
            return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageNotRegistered + "\n" + messageAskGender, replyKeyboardMakerService.getRegistrationChooseGenderInlineMenu());
        }
        pairsNavigationService.refreshFavouritesList(userId);
        User userForShow = pairsNavigationService.navigate(userId, "next");
        bot.executeMessage(
                sendMessageMakerService.createPhotoMessageWithReplyKeyboard(
                        chatId,
                        userForShow.getGender().getValue().concat(", ").concat(userForShow.getName()).concat("\n").concat(userForShow.getLoveSign()),
                        userForShow.getProfileImage(),
                        replyKeyboardMakerService.getNavigationMenuKeyboard()));
        userBotState.setUserBotState(userId, TelegramBotState.VIEW_PAIRS);
        return null;
    }
}
