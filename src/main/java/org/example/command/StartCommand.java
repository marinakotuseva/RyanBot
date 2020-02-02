package org.example.command;

import org.example.Bot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class StartCommand extends BotCommand {
    private static final Logger LOG = Logger.getLogger(Bot.class.getName());

    public StartCommand() {
        super("start", "start using the bot\n");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        LOG.info("Bot started by user " + user.toString());

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Hi. What's on your mind?");

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
