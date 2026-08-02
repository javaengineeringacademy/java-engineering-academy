package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpringBootFundamentalsTest {

    @Test
    void shouldConfigureProperty() {
        SpringBootFundamentalsExample.AutoConfiguration config = new SpringBootFundamentalsExample.AutoConfiguration();
        config.configure("app.name", "TestApp");
        assertEquals("TestApp", config.getProperty("app.name", String.class));
    }

    @Test
    void shouldInitializeStarter() {
        SpringBootFundamentalsExample.AutoConfiguration config = new SpringBootFundamentalsExample.AutoConfiguration();
        SpringBootFundamentalsExample.Starter starter = new SpringBootFundamentalsExample.Starter("web", config);
        starter.initialize();
        assertTrue(starter.isInitialized());
    }
}
