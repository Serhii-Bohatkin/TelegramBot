package bot_practice.bot.command;

import bot_practice.bot.constant.Actions;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartCommandHandler extends BotCommand {

    private final String startSecret;

    public StartCommandHandler(@Value("start") String commandIdentifier,
                               @Value("") String description,
                               @Value("${bot.start-secret}") String startSecret) {
        super(commandIdentifier, description);
        this.startSecret = startSecret;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (strings.length <= 0) {
            return;
        }
        String secret = strings[0];
        if (!startSecret.equals(secret)) {
            return;
        }
        try {
            InlineKeyboardMarkup galaxySombrero = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(
                            InlineKeyboardButton.builder()
                                    .text("BotFather")
                                    .url("https://t.me/BotFather")
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text("Callback button")
                                    .callbackData(Actions.SOME_ACTION)
                                    .build()
                    ))
                    .build();

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("Hi")
                    .replyMarkup(galaxySombrero)
                    .build();
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
