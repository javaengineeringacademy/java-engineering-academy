package academy.javaengineering.springboot;

import java.util.ArrayList;
import java.util.List;

/**
 * Logging - Structured Logging, Log Levels.
 */
public class LoggingExample {

    public enum Level { DEBUG, INFO, WARN, ERROR }

    public static class Logger {
        private final String name;
        private final Level minLevel;
        private final List<String> logs = new ArrayList<>();

        public Logger(String name, Level minLevel) {
            this.name = name;
            this.minLevel = minLevel;
        }

        public void log(Level level, String message) {
            if (level.ordinal() >= minLevel.ordinal()) {
                logs.add("[" + level + "] " + name + ": " + message);
            }
        }

        public void debug(String msg) { log(Level.DEBUG, msg); }
        public void info(String msg) { log(Level.INFO, msg); }
        public void warn(String msg) { log(Level.WARN, msg); }
        public void error(String msg) { log(Level.ERROR, msg); }

        public List<String> getLogs() { return logs; }
    }

    public static void main(String[] args) {
        Logger logger = new Logger("AppLogger", Level.INFO);
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
        System.out.println("Logs: " + logger.getLogs());
    }
}
