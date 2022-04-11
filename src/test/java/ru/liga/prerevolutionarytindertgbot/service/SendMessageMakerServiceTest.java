package ru.liga.prerevolutionarytindertgbot.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class SendMessageMakerServiceTest {

    @Autowired
    private SendMessageMakerService sendMessageMakerService;

    @Test
    public void whenCreateSendMessageCalled_thenSendMessageCreated() {
        SendMessage sendMessage = new SendMessage("1234", "test message");
        sendMessage.enableMarkdown(true);
        assertEquals(sendMessage, sendMessageMakerService.createTextMessage("1234", "test message"));
    }

    @Test
    @Disabled //потому что разные BufferedStream создаются
    public void whenCreateSendPhotoCalled_thenSendPhotoMessageCreated() throws IOException {
        SendPhoto sendPhoto = SendPhoto
                .builder()
                .chatId("1234")
                .photo(new InputFile(new ByteArrayInputStream(Files.readAllBytes(Path.of("src", "test", "resources", "test.png"))), "chatMsg"))
                .caption("chatMsg")
                .build();

        assertEquals(sendPhoto, sendMessageMakerService.createPhotoMessage("1234", "chatMsg", Files.readAllBytes(Path.of("src", "test", "resources", "test.png"))));
    }

    @Test
    public void whenCreateTextMessageWithReplyKeyboardCalled_thenMessageHaveReplyKeyboard() {
        SendMessage expectedSendMessage = SendMessage.builder()
                .chatId("1234")
                .text("ляля")
                .replyMarkup(new ForceReplyKeyboard())
                .build();
        expectedSendMessage.enableMarkdown(true);
        SendMessage actualSendMessage = sendMessageMakerService.createTextMessageWithReplyKeyboard("1234", "ляля", new ForceReplyKeyboard());

        assertNotNull(actualSendMessage.getReplyMarkup(), "Клавиатура отсутсвует");
        assertEquals(expectedSendMessage, actualSendMessage, "Сообщение создается некорреткно");
    }

    @Test
    public void whenCreatePhotoMessageWithReplyKeyboardCalled_thenMessageHaveAllRequiredParams() throws IOException {
        SendPhoto actualSendPhoto = sendMessageMakerService
                .createPhotoMessageWithReplyKeyboard(
                        "1234",
                        "chatMsg",
                        Files.readAllBytes(Path.of("src", "test", "resources", "test.png")),
                        new ForceReplyKeyboard()
                );

        assertNotNull(actualSendPhoto.getReplyMarkup(), "Клавиатура отсутсвует");
        assertNotNull(actualSendPhoto.getPhoto(), "отсутствует фото");
        assertEquals(actualSendPhoto.getChatId(), "1234", "отсутвует идентификатор чата");
        assertEquals(actualSendPhoto.getCaption(), "chatMsg", "отсутствует фото");
    }
}