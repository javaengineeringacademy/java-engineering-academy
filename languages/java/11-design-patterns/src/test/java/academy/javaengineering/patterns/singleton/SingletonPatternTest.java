package academy.javaengineering.patterns.singleton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class SingletonPatternTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instanceField = SingletonExample.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    @DisplayName("Should return the same instance on every call")
    void shouldReturnSameInstance() {
        SingletonExample s1 = SingletonExample.getInstance();
        SingletonExample s2 = SingletonExample.getInstance();
        assertSame(s1, s2, "getInstance() should always return the same object");
    }

    @Test
    @DisplayName("Should return non-null instance")
    void shouldReturnNonNull() {
        SingletonExample instance = SingletonExample.getInstance();
        assertNotNull(instance, "Singleton instance must never be null");
    }

    @Test
    @DisplayName("Should return correct data")
    void shouldReturnCorrectData() {
        SingletonExample instance = SingletonExample.getInstance();
        assertEquals("Singleton Data", instance.getData());
    }

    @Test
    @DisplayName("Should be the only instance via reflection")
    void shouldHavePrivateConstructor() throws Exception {
        var constructor = SingletonExample.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
                "Singleton constructor should be private");
    }

    @Test
    @DisplayName("Should survive multiple getInstance calls returning same reference")
    void shouldSurviveManyCalls() {
        SingletonExample first = SingletonExample.getInstance();
        for (int i = 0; i < 100; i++) {
            assertSame(first, SingletonExample.getInstance());
        }
    }

    @Test
    @DisplayName("Should be thread-safe under concurrent access")
    void shouldBeThreadSafe() throws InterruptedException {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<SingletonExample> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    instances.add(SingletonExample.getInstance());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(1, instances.size(),
                "All threads must get the same singleton instance");
    }

    @Test
    @DisplayName("Should create new instance after field reset via reflection")
    void shouldCreateNewInstanceAfterReset() throws Exception {
        SingletonExample first = SingletonExample.getInstance();
        resetSingleton();
        SingletonExample second = SingletonExample.getInstance();
        assertNotSame(first, second,
                "After resetting the static field, a new instance should be created");
    }

    @Test
    @DisplayName("Should maintain data after re-creation")
    void shouldMaintainDataAfterRecreation() throws Exception {
        SingletonExample first = SingletonExample.getInstance();
        String originalData = first.getData();
        resetSingleton();
        SingletonExample second = SingletonExample.getInstance();
        assertEquals(originalData, second.getData());
    }

    @Test
    @DisplayName("Should work correctly in high-concurrency stress test")
    void shouldBeStressTestSafe() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        Set<SingletonExample> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    instances.add(SingletonExample.getInstance());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        assertEquals(1, instances.size(),
                "Under high concurrency, all threads must see the same instance");
    }
}
