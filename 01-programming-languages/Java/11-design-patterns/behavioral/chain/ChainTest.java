package academy.javaengineering.patterns.behavioral.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChainTest {

    private Handler chain;

    @BeforeEach
    void setUp() {
        Handler auth = new AuthHandler();
        Handler logging = new LoggingHandler();
        Handler validation = new ValidationHandler();
        auth.setNext(logging).setNext(validation);
        chain = auth;
    }

    @Test
    void authHandlerShouldHandleAuthRequests() {
        assertTrue(chain.handle("auth:user"));
    }

    @Test
    void loggingHandlerShouldHandleLogRequests() {
        assertTrue(chain.handle("log:message"));
    }

    @Test
    void validationHandlerShouldHandleValidationRequests() {
        assertTrue(chain.handle("validate:data"));
    }

    @Test
    void unknownRequestShouldNotBeHandled() {
        assertFalse(chain.handle("unknown:data"));
    }

    @Test
    void chainShouldDelegateToNextHandler() {
        assertTrue(chain.handle("log:test"));
    }
}
