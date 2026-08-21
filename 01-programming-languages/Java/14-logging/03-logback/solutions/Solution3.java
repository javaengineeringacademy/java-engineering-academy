package academy.javaengineering.logging.logback.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Solution 3: Logback configuration migration from Log4j 1.x.
 *
 * Equivalent logback.xml:
 *
 * <configuration>
 *     <property name="LOG_PATTERN" 
 *               value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{traceId}] %-5level %logger{36} - %msg%n"/>
 *     
 *     <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
 *         <encoder><pattern>${LOG_PATTERN}</pattern></encoder>
 *     </appender>
 *     
 *     <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
 *         <file>app.log</file>
 *         <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
 *             <fileNamePattern>app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
 *             <maxFileSize>10MB</maxFileSize>
 *             <maxHistory>5</maxHistory>
 *         </rollingPolicy>
 *         <encoder><pattern>${LOG_PATTERN}</pattern></encoder>
 *     </appender>
 *     
 *     <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
 *         <appender-ref ref="FILE"/>
 *         <queueSize>256</queueSize>
 *     </appender>
 *     
 *     <root level="INFO">
 *         <appender-ref ref="CONSOLE"/>
 *         <appender-ref ref="ASYNC_FILE"/>
 *     </root>
 * </configuration>
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    public static void main(String[] args) {
        Solution3 app = new Solution3();
        app.runApplication();
    }

    public void runApplication() {
        String traceId = java.util.UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        try {
            logger.info("Application started");

            for (int i = 0; i < 20; i++) {
                logger.debug("Processing item {}", i);
                if (i % 5 == 0) {
                    logger.warn("Item {} requires attention", i);
                }
            }

            try {
                throw new RuntimeException("Test exception");
            } catch (Exception e) {
                logger.error("Caught exception: {}", e.getMessage(), e);
            }

            logger.info("Application completed");
        } finally {
            MDC.clear();
        }
    }
}
