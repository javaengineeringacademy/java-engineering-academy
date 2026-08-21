package academy.javaengineering.logging.basics.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates proper exception logging patterns.
 */
public class ExceptionLoggingDemo {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingDemo.class);

    public static void main(String[] args) {
        ExceptionLoggingDemo demo = new ExceptionLoggingDemo();
        demo.handleRequests();
    }

    private void handleRequests() {
        processWithRecoveredException();
        processWithWrappedException();
        processWithSuppressedException();
    }

    private void processWithRecoveredException() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            // WARN when we can recover
            logger.warn("Risky operation failed, using fallback: {}", e.getMessage());
        }
    }

    private void processWithWrappedException() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            // ERROR with wrapping for context
            logger.error("Critical failure in processing pipeline", e);
            throw new ProcessingException("Pipeline failed", e);
        }
    }

    private void processWithSuppressedException() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            ProcessingException wrapper = new ProcessingException("Primary failed", e);
            try {
                alternativeOperation();
            } catch (Exception alt) {
                wrapper.addSuppressed(alt);
            }
            logger.error("All operations failed", wrapper);
        }
    }

    private void riskyOperation() {
        throw new RuntimeException("Simulated database connection timeout");
    }

    private void alternativeOperation() {
        throw new RuntimeException("Simulated cache unavailability");
    }

    static class ProcessingException extends RuntimeException {
        ProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
