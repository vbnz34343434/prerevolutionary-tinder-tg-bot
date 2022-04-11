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
import ru.liga.prerevolutionarytindertgbot.service.SearchPairService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

@Component
public class NavigateLeftHandler implements CommandHandler {
    private final UserBotState userBotState;
    private final SearchPairService searchPairService;
    private final PairsNavigationService pairsNavigationService;
    private final SendMessageMakerService sendMessageMakerService;
    private final TelegramTinderBot bot;

    public NavigateLeftHandler(UserBotState userBotState, SearchPairService searchPairService, PairsNavigationService pairsNavigationService, SendMessageMakerService sendMessageMakerService, @Lazy TelegramTinderBot bot) {
        this.userBotState = userBotState;
        this.searchPairService = searchPairService;
        this.pairsNavigationService = pairsNavigationService;
        this.sendMessageMakerService = sendMessageMakerService;
        this.bot = bot;
    }

    @Override
    public String getName() {
        return "Влево";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        User userForShow;
        TelegramBotState state = userBotState.getUserBotState(userId);

        switch (state) {
            case IN_SEARCH:
                userForShow = searchPairService.searchNext(userId, true);
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessage(
                                chatId,
                                userForShow.getGender().getValue() + ", " + userForShow.getName(),
                                userForShow.getProfileImage()));
                break;
            case VIEW_PAIRS:
                userForShow = pairsNavigationService.navigate(userId, "previous");
                bot.executeMessage(
                        sendMessageMakerService.createPhotoMessage(
                                chatId,
                                userForShow.getGender().getValue().concat(", ").concat(userForShow.getName()).concat("\n").concat(userForShow.getLoveSign()),
                                userForShow.getProfileImage()));
                break;
            default:
                throw new RuntimeException("Неизвестная команда");
        }
        return null;
    }
}
