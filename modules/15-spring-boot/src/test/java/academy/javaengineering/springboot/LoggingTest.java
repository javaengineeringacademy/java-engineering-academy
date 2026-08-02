package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoggingTest {

    @Test
    void shouldLogAboveMinLevel() {
        LoggingExample.Logger logger = new LoggingExample.Logger("Test", LoggingExample.Level.INFO);
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        assertEquals(3, logger.getLogs().size());
    }

    @Test
    void shouldFilterBelowMinLevel() {
        LoggingExample.Logger logger = new LoggingExample.Logger("Test", LoggingExample.Level.ERROR);
        logger.info("info");
        logger.error("error");
        assertEquals(1, logger.getLogs().size());
    }
}
