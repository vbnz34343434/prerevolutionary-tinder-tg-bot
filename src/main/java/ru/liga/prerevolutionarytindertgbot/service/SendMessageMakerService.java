package ru.liga.prerevolutionarytindertgbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.ByteArrayInputStream;

@Service
public class SendMessageMakerService {

    public SendMessage createTextMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    public SendMessage createTextMessageWithReplyKeyboard(String chatId, String message, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public SendPhoto createPhotoMessage(String chatId, String caption, byte[] image) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(image), "chatMsg"));
        sendPhoto.setCaption(caption);
        return sendPhoto;
    }

    public SendPhoto createPhotoMessageWithReplyKeyboard(String chatId, String caption, byte[] image, ReplyKeyboard keyboard) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(image), "chatMsg"));
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(keyboard);
        return sendPhoto;
    }
}