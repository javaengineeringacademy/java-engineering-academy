package academy.javaengineering.logging.logback.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3: Implement a Logback configuration migration.
 *
 * Given this old Log4j 1.x configuration:
 * 
 * log4j.rootLogger=INFO, CONSOLE, FILE
 * log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
 * log4j.appender.FILE=org.apache.log4j.RollingFileAppender
 * log4j.appender.FILE.File=app.log
 * log4j.appender.FILE.MaxFileSize=10MB
 * log4j.appender.FILE.MaxBackupIndex=5
 *
 * Convert to equivalent Logback configuration and implement:
 * 1. Equivalent logback.xml
 * 2. Add MDC support
 * 3. Add async logging
 * 4. Improve the pattern with more context
 * 5. Implement the application code that uses the new configuration
 */
public class Exercise3 {

    // TODO: Create logger with proper naming
    // TODO: Implement application that demonstrates the migrated configuration

    public static void main(String[] args) {
        // TODO: Demonstrate logging with new configuration
    }
}
