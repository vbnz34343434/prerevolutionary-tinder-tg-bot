package ru.liga.prerevolutionarytindertgbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;
import ru.liga.prerevolutionarytindertgbot.enums.LookingFor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReplyKeyboardMakerService {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Поиск"));
        row.add(new KeyboardButton("Анкета"));
        row.add(new KeyboardButton("Любимцы"));

        return getReplyKeyboardMarkup(row);
    }

    public ReplyKeyboardMarkup getNavigationMenuKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Влево"));
        row.add(new KeyboardButton("Меню"));
        row.add(new KeyboardButton("Вправо"));

        return getReplyKeyboardMarkup(row);
    }

    public ReplyKeyboardMarkup getChangeProfileMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        row1.add(new KeyboardButton("Изменить имя"));
        row2.add(new KeyboardButton("Изменить пол"));
        row3.add(new KeyboardButton("Изменить инфо о себе"));
        row4.add(new KeyboardButton("Изменить кого ищете"));
        row5.add(new KeyboardButton("Меню"));

        return getReplyKeyboardMarkup(row1, row2, row3, row4, row5);
    }

    public InlineKeyboardMarkup getRegistrationChooseGenderInlineMenu() {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(Gender.MALE.getValue());
        button1.setCallbackData("gender male");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(Gender.FEMALE.getValue());
        button2.setCallbackData("gender female");

        return getInlineKeyboardMarkup(button1, button2);
    }

    public InlineKeyboardMarkup getRegistrationChooseLookingForInlineMenu() {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(LookingFor.MALES.getValue());
        button1.setCallbackData("looking for males");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(LookingFor.FEMALES.getValue());
        button2.setCallbackData("looking for females");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(LookingFor.ALL.getValue());
        button3.setCallbackData("looking for all");

        return getInlineKeyboardMarkup(button1, button2, button3);
    }

    public InlineKeyboardMarkup getEditGenderInlineMenu() {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(Gender.MALE.getValue());
        button1.setCallbackData("change gender to male");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(Gender.FEMALE.getValue());
        button2.setCallbackData("change gender to female");

        return getInlineKeyboardMarkup(button1, button2);
    }

    public InlineKeyboardMarkup getEditLookingForInlineMenu() {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(LookingFor.MALES.getValue());
        button1.setCallbackData("change looking for to males");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(LookingFor.FEMALES.getValue());
        button2.setCallbackData("change looking for to females");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(LookingFor.ALL.getValue());
        button3.setCallbackData("change looking for to all");

        return getInlineKeyboardMarkup(button1, button2, button3);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(KeyboardRow...row) {
        List<KeyboardRow> keyboard = new ArrayList<>(Arrays.asList(row));

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(InlineKeyboardButton...buttons) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>(Arrays.asList(buttons));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(keyboardButtonsRow));

        return inlineKeyboardMarkup;
    }
}