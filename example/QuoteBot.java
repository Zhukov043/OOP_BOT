package org.example;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class QuoteBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public QuoteBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chat_id = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();
            if (userMessage.equals("/start")){
                sendMessage(QuoteMessage.MenuMessage(chat_id));
            }
            else{
                SendMessage message = new SendMessage(chat_id, "Введите команду /start, чтобы начать работу с ботом");
                sendMessage(message);
            }
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            String chat_id = callbackQuery.getMessage().getChatId().toString();
            switch (callbackData) {
                case "allcurrency_callback" -> {
                    String text = QuoteServices.getQuotes("http://www.cbr.ru/scripts/XML_daily.asp?", 1);
                    SendMessage message = new SendMessage(chat_id, text);
                    sendMessage(message);
                }
                case "metals_callback" -> {
                    String text = QuoteServices.getQuotes("http://www.cbr.ru/scripts/xml_metall.asp?date_req1=26/12/2024&date_req2=26/12/2024", 2);
                    SendMessage message = new SendMessage(chat_id, text);
                    sendMessage(message);
                }
                case "currency_callback" -> {
                    String text = QuoteServices.getQuotes("http://www.cbr.ru/scripts/XML_daily.asp?", 3);
                    SendMessage message = new SendMessage(chat_id, text);
                    sendMessage(message);
                }
            }
            sendMessage(QuoteMessage.MenuMessage(chat_id));
        }
        else if (update.hasMessage() && !update.getMessage().hasText()){
            String chat_id = update.getMessage().getChatId().toString();
            String text = "Введите команду /start, чтобы начать работу с ботом";
            SendMessage message = new SendMessage(chat_id, text);
            sendMessage(message);
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}

