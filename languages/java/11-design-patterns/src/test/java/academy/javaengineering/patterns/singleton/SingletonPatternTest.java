package academy.javaengineering.patterns.singleton;

import academy.javaengineering.patterns.singleton.SingletonExample.EagerSingleton;
import academy.javaengineering.patterns.singleton.SingletonExample.LazySingleton;
import academy.javaengineering.patterns.singleton.SingletonExample.DCLSingleton;
import academy.javaengineering.patterns.singleton.SingletonExample.EnumSingleton;
import academy.javaengineering.patterns.singleton.SingletonExample.BillPughSingleton;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class SingletonPatternTest {

    // ========================================
    // Eager Singleton Tests
    // ========================================

    @Test
    @DisplayName("EagerSingleton should return the same instance on every call")
    void eagerShouldReturnSameInstance() {
        EagerSingleton s1 = EagerSingleton.getInstance();
        EagerSingleton s2 = EagerSingleton.getInstance();
        assertSame(s1, s2, "getInstance() should always return the same object");
    }

    @Test
    @DisplayName("EagerSingleton should return non-null instance")
    void eagerShouldReturnNonNull() {
        EagerSingleton instance = EagerSingleton.getInstance();
        assertNotNull(instance, "Singleton instance must never be null");
    }

    @Test
    @DisplayName("EagerSingleton should have private constructor")
    void eagerShouldHavePrivateConstructor() throws Exception {
        var constructor = EagerSingleton.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
                "Singleton constructor should be private");
    }

    // ========================================
    // Lazy Singleton Tests
    // ========================================

    @Test
    @DisplayName("LazySingleton should return the same instance on every call")
    void lazyShouldReturnSameInstance() {
        LazySingleton s1 = LazySingleton.getInstance();
        LazySingleton s2 = LazySingleton.getInstance();
        assertSame(s1, s2, "getInstance() should always return the same object");
    }

    @Test
    @DisplayName("LazySingleton should return non-null instance")
    void lazyShouldReturnNonNull() {
        LazySingleton instance = LazySingleton.getInstance();
        assertNotNull(instance, "Singleton instance must never be null");
    }

    @Test
    @DisplayName("LazySingleton should survive multiple getInstance calls")
    void lazyShouldSurviveManyCalls() {
        LazySingleton first = LazySingleton.getInstance();
        for (int i = 0; i < 100; i++) {
            assertSame(first, LazySingleton.getInstance());
        }
    }

    // ========================================
    // DCL Singleton Tests
    // ========================================

    @Test
    @DisplayName("DCLSingleton should return the same instance on every call")
    void dclShouldReturnSameInstance() {
        DCLSingleton s1 = DCLSingleton.getInstance();
        DCLSingleton s2 = DCLSingleton.getInstance();
        assertSame(s1, s2, "getInstance() should always return the same object");
    }

    @Test
    @DisplayName("DCLSingleton should be thread-safe under concurrent access")
    void dclShouldBeThreadSafe() throws InterruptedException {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<DCLSingleton> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    instances.add(DCLSingleton.getInstance());
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

    // ========================================
    // Enum Singleton Tests
    // ========================================

    @Test
    @DisplayName("EnumSingleton should return the same instance")
    void enumShouldReturnSameInstance() {
        EnumSingleton e1 = EnumSingleton.INSTANCE;
        EnumSingleton e2 = EnumSingleton.INSTANCE;
        assertSame(e1, e2);
    }

    @Test
    @DisplayName("EnumSingleton should return correct data")
    void enumShouldReturnCorrectData() {
        assertEquals("Enum Singleton Data", EnumSingleton.INSTANCE.getData());
    }

    // ========================================
    // Bill Pugh Singleton Tests
    // ========================================

    @Test
    @DisplayName("BillPughSingleton should return the same instance on every call")
    void billPughShouldReturnSameInstance() {
        BillPughSingleton s1 = BillPughSingleton.getInstance();
        BillPughSingleton s2 = BillPughSingleton.getInstance();
        assertSame(s1, s2, "getInstance() should always return the same object");
    }

    @Test
    @DisplayName("BillPughSingleton should return non-null instance")
    void billPughShouldReturnNonNull() {
        BillPughSingleton instance = BillPughSingleton.getInstance();
        assertNotNull(instance, "Singleton instance must never be null");
    }

    @Test
    @DisplayName("BillPughSingleton should be thread-safe under high concurrency")
    void billPughShouldBeStressTestSafe() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        Set<BillPughSingleton> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    instances.add(BillPughSingleton.getInstance());
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
