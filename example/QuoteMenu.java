package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class QuoteMenu {
    public static SendMessage MenuMessage(String chatId) {
        String text = "Выберите, что вас интересует: ";
        SendMessage message = new SendMessage(chatId, text);

        List<InlineKeyboardRow> keyboard = new ArrayList<>();

        InlineKeyboardRow row = new InlineKeyboardRow();
        InlineKeyboardButton button1 = new InlineKeyboardButton("Курс всех валют");
        button1.setCallbackData("allcurrency_callback");
        row.add(button1);

        InlineKeyboardRow row1 = new InlineKeyboardRow();
        InlineKeyboardButton button2 = new InlineKeyboardButton("Курс драгоценных металлов");
        button2.setCallbackData("metals_callback");
        row1.add(button2);

        InlineKeyboardRow row2 = new InlineKeyboardRow();
        InlineKeyboardButton button3 = new InlineKeyboardButton("Курс основных валют");
        button3.setCallbackData("currency_callback");
        row2.add(button3);

        keyboard.add(row);
        keyboard.add(row1);
        keyboard.add(row2);

        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup
                .builder()
                .keyboard(keyboard)
                .build();

        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
