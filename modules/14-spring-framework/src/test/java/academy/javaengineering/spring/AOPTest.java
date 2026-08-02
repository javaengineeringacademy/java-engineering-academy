package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AOPTest {

    @Test
    void shouldLogBeforeAndAfter() {
        AOPExample.LoggingAspect aspect = new AOPExample.LoggingAspect();
        AOPExample.UserService service = new AOPExample.UserService(aspect);
        service.createUser("John");
        List<String> logs = aspect.getLogs();
        assertEquals(2, logs.size());
        assertTrue(logs.get(0).contains("Before"));
        assertTrue(logs.get(1).contains("After"));
    }
}
