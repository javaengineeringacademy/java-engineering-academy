package academy.javaengineering.logging.mdc.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Exercise 2: Implement MDC-aware executor service.
 *
 * Requirements:
 * 1. Create an MdcExecutorService that wraps ThreadPoolExecutor
 * 2. Automatically copy MDC from submitting thread to task thread
 * 3. Clean up MDC after task completion
 * 4. Support all ExecutorService methods
 *
 * Usage:
 * ExecutorService executor = new MdcExecutorService(Executors.newFixedThreadPool(5));
 * executor.submit(() -> {
 *     // MDC is automatically available here
 *     logger.info("Processing with context");
 * });
 */
public class Exercise2 {

    // TODO: Create MdcExecutorService wrapper class
    // TODO: Demonstrate usage with MDC context

    public static void main(String[] args) {
        // TODO: Create MDC-aware executor
        // TODO: Submit tasks with MDC context
        // TODO: Verify MDC is available in tasks
    }
}
