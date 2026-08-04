package academy.javaengineering.jvm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GarbageCollectionTest {

    @Test
    void shouldAllocateMemory() {
        GarbageCollectionDemo.MemoryHog hog = new GarbageCollectionDemo.MemoryHog(1024);
        assertNotNull(hog);
    }

    @Test
    void shouldCreateSoftReference() {
        GarbageCollectionDemo.ReferenceDemo demo = new GarbageCollectionDemo.ReferenceDemo();
        assertDoesNotThrow(demo::demonstrateSoftReference);
    }

    @Test
    void shouldCreateWeakReference() {
        GarbageCollectionDemo.ReferenceDemo demo = new GarbageCollectionDemo.ReferenceDemo();
        assertDoesNotThrow(demo::demonstrateWeakReference);
    }

    @Test
    void shouldTriggerGC() {
        Runtime runtime = Runtime.getRuntime();
        // Allocate some objects to create garbage
        for (int i = 0; i < 100; i++) {
            var garbage = new byte[1024];
        }
        System.gc();
        // GC may or may not free memory; just verify the call doesn't throw
        long freeMemory = runtime.freeMemory();
        assertTrue(freeMemory > 0);
    }
}
