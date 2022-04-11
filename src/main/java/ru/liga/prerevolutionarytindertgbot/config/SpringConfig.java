package ru.liga.prerevolutionarytindertgbot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramTinderBot;
import ru.liga.prerevolutionarytindertgbot.bot.handler.CallbackQueryHandler;
import ru.liga.prerevolutionarytindertgbot.bot.handler.MessageHandler;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SpringConfig {
    private final TelegramBotConfig telegramConfig;

    @Value("${user.service.base.url}")
    private String usersWebClientBaseUrl;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public TelegramTinderBot springWebhookBot(SetWebhook setWebhook,
                                              MessageHandler messageHandler,
                                              CallbackQueryHandler callbackQueryHandler) {
        TelegramTinderBot bot = new TelegramTinderBot(setWebhook, messageHandler, callbackQueryHandler);

        bot.setBotPath(telegramConfig.getWebhookPath());
        bot.setBotUsername(telegramConfig.getBotName());
        bot.setBotToken(telegramConfig.getBotToken());

        return bot;
    }

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(usersWebClientBaseUrl)
                .build();
    }
}