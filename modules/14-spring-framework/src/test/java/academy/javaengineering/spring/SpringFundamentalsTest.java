package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpringFundamentalsTest {

    @Test
    void shouldRegisterAndRetrieveBean() {
        SpringFundamentalsExample.ApplicationContext context = new SpringFundamentalsExample.ApplicationContext();
        SpringFundamentalsExample.MessageService service = new SpringFundamentalsExample.HelloService();
        context.registerBean("helloService", service);
        assertEquals(service, context.getBean(SpringFundamentalsExample.MessageService.class));
    }

    @Test
    void shouldReturnNullForMissingBean() {
        SpringFundamentalsExample.ApplicationContext context = new SpringFundamentalsExample.ApplicationContext();
        assertNull(context.getBean("missing"));
    }
}
