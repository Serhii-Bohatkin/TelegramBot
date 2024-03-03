package bot_practice.bot.command;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

@RequiredArgsConstructor
public abstract class BaseTextCommand implements IBotCommand {
    private final String textCommandIdentifier;
    private final String description;

    @Override
    public String getCommandIdentifier() {
        return textCommandIdentifier;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
