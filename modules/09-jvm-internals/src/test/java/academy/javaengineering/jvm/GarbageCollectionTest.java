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
        long freeBefore = runtime.freeMemory();
        System.gc();
        long freeAfter = runtime.freeMemory();
        assertTrue(freeAfter >= freeBefore - 1024);
    }
}
