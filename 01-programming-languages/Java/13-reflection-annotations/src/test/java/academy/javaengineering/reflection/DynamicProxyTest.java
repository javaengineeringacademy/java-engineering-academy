package academy.javaengineering.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dynamic Proxy Tests")
class DynamicProxyTest {

    interface TestService {
        String echo(String input);
        int compute(int a, int b);
        @Deprecated
        String legacyMethod();
    }

    static class TestServiceImpl implements TestService {
        @Override
        public String echo(String input) {
            return "echo:" + input;
        }

        @Override
        public int compute(int a, int b) {
            return a + b;
        }

        @Override
        public String legacyMethod() {
            return "legacy";
        }
    }

    @Test
    @DisplayName("Should create a basic dynamic proxy")
    void testBasicProxyCreation() {
        TestService real = new TestServiceImpl();
        TestService proxy = (TestService) Proxy.newProxyInstance(
                TestService.class.getClassLoader(),
                new Class<?>[]{TestService.class},
                (p, method, args) -> method.invoke(real, args)
        );

        assertNotNull(proxy);
        assertTrue(Proxy.isProxyClass(proxy.getClass()));
    }

    @Test
    @DisplayName("Should intercept method calls via proxy")
    void testMethodInterception() {
        List<String> intercepted = new ArrayList<>();
        TestService real = new TestServiceImpl();
        TestService proxy = (TestService) Proxy.newProxyInstance(
                TestService.class.getClassLoader(),
                new Class<?>[]{TestService.class},
                (p, method, args) -> {
                    intercepted.add(method.getName());
                    return method.invoke(real, args);
                }
        );

        proxy.echo("hello");
        proxy.compute(1, 2);

        assertEquals(2, intercepted.size());
        assertEquals("echo", intercepted.get(0));
        assertEquals("compute", intercepted.get(1));
    }

    @Test
    @DisplayName("Should return correct results through proxy")
    void testProxyReturnsCorrectResult() {
        TestService real = new TestServiceImpl();
        TestService proxy = DynamicProxyExample.createLoggingProxy(real, TestService.class);

        assertEquals("echo:test", proxy.echo("test"));
        assertEquals(5, proxy.compute(2, 3));
    }

    @Test
    @DisplayName("Should track invocation count via proxy")
    void testInvocationCounting() {
        AtomicInteger count = new AtomicInteger(0);
        TestService real = new TestServiceImpl();
        TestService proxy = (TestService) Proxy.newProxyInstance(
                TestService.class.getClassLoader(),
                new Class<?>[]{TestService.class},
                (p, method, args) -> {
                    count.incrementAndGet();
                    return method.invoke(real, args);
                }
        );

        proxy.echo("a");
        proxy.echo("b");
        proxy.compute(1, 2);

        assertEquals(3, count.get());
    }

    @Test
    @DisplayName("Should identify proxy class and interfaces")
    void testProxyIntrospection() {
        TestService real = new TestServiceImpl();
        TestService proxy = DynamicProxyExample.createLoggingProxy(real, TestService.class);

        assertTrue(Proxy.isProxyClass(proxy.getClass()));
        assertArrayEquals(new Class<?>[]{TestService.class}, proxy.getClass().getInterfaces());
    }

    @Test
    @DisplayName("Should throw exception from proxy handler")
    void testProxyExceptionHandling() {
        TestService real = new TestServiceImpl();
        TestService proxy = (TestService) Proxy.newProxyInstance(
                TestService.class.getClassLoader(),
                new Class<?>[]{TestService.class},
                (p, method, args) -> {
                    if (method.getName().equals("echo")) {
                        throw new RuntimeException("Proxy error");
                    }
                    return method.invoke(real, args);
                }
        );

        assertThrows(RuntimeException.class, () -> proxy.echo("fail"));
        assertEquals(5, proxy.compute(2, 3)); // Other methods still work
    }

    @Test
    @DisplayName("Should create proxy via DynamicProxyExample utility")
    void testUtilityProxyCreation() {
        TestService real = new TestServiceImpl();
        TestService proxy = DynamicProxyExample.createLoggingProxy(real, TestService.class);

        assertNotNull(proxy);
        assertEquals("echo:utility", proxy.echo("utility"));
    }
}
