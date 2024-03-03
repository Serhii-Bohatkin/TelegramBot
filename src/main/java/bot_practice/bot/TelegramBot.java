package bot_practice.bot;

import bot_practice.bot.constant.Actions;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final String userName;

    public TelegramBot(@Value("${bot.token}") String botToken,
                       @Value("${bot.username}") String userName) {
        super(botToken);
        this.userName = userName;
    }


    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            switch (callbackQuery.getData()) {
                case Actions.SOME_ACTION -> {
                    try {
                        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup
                                .builder()
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow(List.of(
                                        new KeyboardButton("Reply keyboard button")
                                )))
                                .build();
                        sendApiMethod(SendMessage
                                .builder()
                                .chatId(callbackQuery.getMessage().getChatId())
                                .text("After callback message")
                                .replyMarkup(keyboardMarkup)
                                .build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                sendApiMethod(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Answer callback query")
                        .build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            IBotCommand command = getRegisteredCommand(text);
            if (Objects.nonNull(command)) {
                command.processMessage(this, message, new String[]{});
            }
        }
    }
}
