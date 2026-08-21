package academy.javaengineering.logging.log4j2.solutions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Solution 1: High-throughput Log4j 2 configuration and usage.
 *
 * Equivalent log4j2.xml:
 * 
 * <Configuration status="WARN">
 *     <Properties>
 *         <Property name="LOG_DIR">/var/log/myapp</Property>
 *     </Properties>
 *     <Appenders>
 *         <Console name="Console" target="SYSTEM_OUT">
 *             <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
 *         </Console>
 *         <RollingFile name="RollingFile"
 *                      fileName="${LOG_DIR}/app.log"
 *                      filePattern="${LOG_DIR}/app.%d{yyyy-MM-dd}.%i.log.gz">
 *             <JsonLayout compact="true" eventEol="true">
 *                 <KeyValuePair key="service" value="myapp"/>
 *             </JsonLayout>
 *             <Policies>
 *                 <SizeBasedTriggeringPolicy size="100MB"/>
 *                 <TimeBasedTriggeringPolicy interval="1"/>
 *             </Policies>
 *             <DefaultRolloverStrategy max="30"/>
 *         </RollingFile>
 *     </Appenders>
 *     <Loggers>
 *         <AsyncLogger name="com.myapp" level="DEBUG"/>
 *         <Root level="INFO">
 *             <AppenderRef ref="Console"/>
 *             <AppenderRef ref="RollingFile"/>
 *         </Root>
 *     </Loggers>
 * </Configuration>
 */
public class Solution1 {

    private static final Logger logger = LogManager.getLogger(Solution1.class);

    public static void main(String[] args) {
        Solution1 app = new Solution1();
        app.runHighThroughputDemo();
    }

    public void runHighThroughputDemo() {
        logger.info("Starting high-throughput logging demo");

        long start = System.currentTimeMillis();
        int iterations = 100000;

        for (int i = 0; i < iterations; i++) {
            logger.debug("Processing item {}", i);
            if (i % 10000 == 0) {
                logger.info("Progress: {} items processed", i);
            }
        }

        long duration = System.currentTimeMillis() - start;
        logger.info("Completed {} iterations in {}ms ({} ops/sec)",
                iterations, duration, (iterations * 1000L) / duration);
    }
}
