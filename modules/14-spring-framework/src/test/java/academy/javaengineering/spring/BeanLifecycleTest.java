package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BeanLifecycleTest {

    @Test
    void shouldFollowLifecyclePhases() {
        BeanLifecycleExample.MyBean bean = new BeanLifecycleExample.MyBean();
        assertEquals("created", bean.getStatus());
        bean.init();
        assertEquals("initialized", bean.getStatus());
        bean.destroy();
        assertEquals("destroyed", bean.getStatus());
    }

    @Test
    void shouldTrackEvents() {
        BeanLifecycleExample.MyBean bean = new BeanLifecycleExample.MyBean();
        bean.init();
        bean.destroy();
        assertEquals(2, bean.getEvents().size());
    }
}
