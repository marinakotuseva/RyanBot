package org.example;

import org.example.command.StartCommand;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingCommandBot {
    private static final Logger LOG = Logger.getLogger(Bot.class.getName());
    private static final String BOT_NAME = "Hey, girl";
    private static final String[] urls = {
            "https://m.media-amazon.com/images/M/MV5BMTQzMjkwNTQ2OF5BMl5BanBnXkFtZTgwNTQ4MTQ4MTE@._V1_.jpg",
            "https://img5.goodfon.ru/wallpaper/nbig/c/40/ryan-gosling-vzgliad-muzhchina.jpg",
            "https://magazin.lufthansa.com/content/uploads/2019/04/ryan-gosling-477x596.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/RyanGosling07TIFF.jpg/170px-RyanGosling07TIFF.jpg"};

    public Bot(DefaultBotOptions options) {
        super(options);

        HelpCommand helpCommand = new HelpCommand();
        register(helpCommand);
        StartCommand startCommand = new StartCommand();
        register(startCommand);

        registerDefaultAction(((absSender, message) -> {

            LOG.warning("Unknown command received: " + message.getText());

            SendMessage replyMessage = new SendMessage();
            replyMessage.setChatId(message.getChatId());
            replyMessage.setText(message.getText() + " command not found!");

            try {
                absSender.execute(replyMessage);
            } catch (TelegramApiException e) {
                LOG.severe("Error while replying to user " + message.getFrom());
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public String getBotToken() {

        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/bot.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty("token");
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        LOG.info("Processing non-command update...");

        Message msg = update.getMessage();

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(msg.getChatId());
        sendChatAction.setAction(ActionType.TYPING);
        try {
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        String url = urls[(int) ((Math.random() * (urls.length - 1)) + 1)];
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(msg.getChatId());
        sendPhotoRequest.setPhoto(url);

        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
