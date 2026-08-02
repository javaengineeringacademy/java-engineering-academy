package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BeanScopesTest {

    @Test
    void singletonShouldReturnSameInstance() {
        BeanScopesExample.SingletonScope s1 = BeanScopesExample.SingletonScope.getInstance();
        BeanScopesExample.SingletonScope s2 = BeanScopesExample.SingletonScope.getInstance();
        assertSame(s1, s2);
    }

    @Test
    void prototypeShouldReturnDifferentInstances() {
        BeanScopesExample.PrototypeScope prototype = new BeanScopesExample.PrototypeScope();
        Object p1 = prototype.create();
        Object p2 = prototype.create();
        assertNotSame(p1, p2);
    }
}
