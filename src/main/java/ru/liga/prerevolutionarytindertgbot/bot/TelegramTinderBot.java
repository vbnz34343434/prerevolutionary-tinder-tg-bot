package ru.liga.prerevolutionarytindertgbot.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CallbackQueryHandler;
import ru.liga.prerevolutionarytindertgbot.bot.handler.MessageHandler;

@Getter
@Setter
@Slf4j
public class TelegramTinderBot extends SpringWebhookBot {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private String botPath;
    private String botUsername;
    private String botToken;

    @Autowired
    public TelegramTinderBot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            log.error("Exception", e);
            if (update.hasCallbackQuery()) {
                return new SendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), e.getMessage());
            } else if (update.hasMessage()) {
                return new SendMessage(update.getMessage().getChatId().toString(), e.getMessage());
            } else return null;
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        log.info("update received: {}", update);

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }
        return null;
    }

    public void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("An exception has been occurred while sending response message");
        }
    }

    public void executeMessage(SendPhoto message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram exception:", e);
            throw new RuntimeException("An exception has been occurred while sending response message");
        }
    }
}
