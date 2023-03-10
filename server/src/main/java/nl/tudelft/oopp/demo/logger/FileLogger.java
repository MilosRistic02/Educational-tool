package nl.tudelft.oopp.demo.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLogger {
    private static Logger logger = Logger.getLogger("AdminLog");
    private static FileHandler fh;

    public static void addMessage(String message) {
        logger.info(message);
    }

    /**
     * Initializes a new FileLogger. Every new message is appended
     * in the specified file.
     */
    public static void init() {
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("./Logs/MyLogFile.log", true);
            logger.addHandler(fh);
            fh.setFormatter(new MyFormatter());
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
