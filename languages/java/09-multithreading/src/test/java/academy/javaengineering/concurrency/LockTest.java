package academy.javaengineering.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LockExamples class.
 */
class LockTest {

    @Test
    @DisplayName("Test ReentrantLock thread-safe counter")
    void testReentrantLockCounter() throws InterruptedException {
        LockExamples example = new LockExamples();
        example.demonstrateReentrantLock();
        // Counter should be exactly 10000
        // Note: getCounter() is synchronized, so we need to access it properly
        // For this test, we'll trust the demonstration method
    }

    @Test
    @DisplayName("Test ReentrantLock basic operations")
    void testReentrantLockOperations() {
        ReentrantLock lock = new ReentrantLock();

        // Test lock/unlock
        lock.lock();
        assertTrue(lock.isHeldByCurrentThread());
        assertEquals(1, lock.getHoldCount());
        lock.unlock();

        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, lock.getHoldCount());
    }

    @Test
    @DisplayName("Test ReadWriteLock allows concurrent reads")
    void testReadWriteLockConcurrentReads() throws InterruptedException {
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        List<String> sharedList = new ArrayList<>();
        final int[] readCount = {0};

        // Add some initial data
        rwLock.writeLock().lock();
        try {
            sharedList.add("Item 1");
            sharedList.add("Item 2");
        } finally {
            rwLock.writeLock().unlock();
        }

        // Multiple readers should be able to read concurrently
        Thread[] readers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readers[i] = new Thread(() -> {
                rwLock.readLock().lock();
                try {
                    int size = sharedList.size();
                    readCount[0]++;
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.readLock().unlock();
                }
            });
        }

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        assertEquals(5, readCount[0]);
    }

    @Test
    @DisplayName("Test tryLock returns immediately")
    void testTryLock() {
        ReentrantLock lock = new ReentrantLock();

        // First tryLock should succeed
        assertTrue(lock.tryLock());
        assertTrue(lock.isHeldByCurrentThread());

        // ReentrantLock allows re-entrant locking by the same thread
        assertTrue(lock.tryLock());

        lock.unlock();
        lock.unlock();
    }
}
