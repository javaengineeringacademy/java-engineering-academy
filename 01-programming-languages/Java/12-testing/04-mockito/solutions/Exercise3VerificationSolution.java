package academy.javaengineering.testing.mockito.solutions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise3VerificationSolution {

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
        Service service = new Service(logger);

        service.process("data");

        verify(logger).info("Processing: data");
        verify(logger).info("Done");
    }

    @Test
    void shouldNotLogDebugInProduction() {
        Service service = new Service(logger);

        service.process("data");

        verify(logger, never()).debug(anyString());
    }
}
