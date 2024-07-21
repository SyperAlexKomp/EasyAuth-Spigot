package ua.starman.easylogin.filters;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class CommandsFilter implements Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        String message = record.getMessage();
        if (message != null) {
            if (message.toLowerCase().contains("/reg ") || message.toLowerCase().contains("/log ")) {
                String playerName = record.getMessage().split(" ")[0];
                record.setMessage(playerName + " issued an auth command");
            }
        }
        return true;
    }
}
