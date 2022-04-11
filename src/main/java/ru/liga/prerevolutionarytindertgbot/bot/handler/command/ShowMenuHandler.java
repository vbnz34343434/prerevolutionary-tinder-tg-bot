package ru.liga.prerevolutionarytindertgbot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CommandHandler;
import ru.liga.prerevolutionarytindertgbot.service.ReplyKeyboardMakerService;
import ru.liga.prerevolutionarytindertgbot.service.SendMessageMakerService;

import static ru.liga.prerevolutionarytindertgbot.util.Constant.messageMenuButton;

@Component
@RequiredArgsConstructor
public class ShowMenuHandler implements CommandHandler {
    private final SendMessageMakerService sendMessageMakerService;
    private final ReplyKeyboardMakerService replyKeyboardMakerService;

    @Override
    public String getName() {
        return "Меню";
    }

    @Override
    public BotApiMethod<?> handle(long userId, String chatId, String commandText) {
        return sendMessageMakerService.createTextMessageWithReplyKeyboard(chatId, messageMenuButton, replyKeyboardMakerService.getMainMenuKeyboard());
    }
}
