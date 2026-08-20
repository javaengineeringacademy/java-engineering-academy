package academy.javaengineering.testing.mockito.practices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Exercise 3: Verification
 *
 * Tasks:
 * 1. Verify method called with specific arguments
 * 2. Verify method never called
 * 3. Verify call count (times, atLeast, atMost)
 * 4. Verify no more interactions
 */
@ExtendWith(MockitoExtension.class)
class Exercise3Verification {

    interface Logger {
        void info(String message);
        void error(String message);
        void debug(String message);
    }

    static class Service {
        private final Logger logger;
        Service(Logger logger) { this.logger = logger; }
        void process(String input) {
            logger.info("Processing: " + input);
            // process...
            logger.info("Done");
        }
        void handleError(String error) {
            logger.error(error);
        }
    }

    @Mock
    private Logger logger;

    @Test
    void shouldLogProcessingSteps() {
        // Arrange
        Service service = new Service(logger);

        // Act
        service.process("data");

        // Assert: verify logging calls
    }

    @Test
    void shouldNotLogDebugInProduction() {
        // Arrange
        Service service = new Service(logger);

        // Act
        service.process("data");

        // Assert: verify debug was never called
    }
}
