package academy.javaengineering.jvm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemoryModelTest {

    @Test
    void shouldAllocateLargeArray() {
        MemoryModelExample.HeapDemo heapDemo = new MemoryModelExample.HeapDemo();
        assertDoesNotThrow(() -> heapDemo.allocateLargeArray(1024));
    }

    @Test
    void shouldPerformRecursion() {
        MemoryModelExample.StackDemo stackDemo = new MemoryModelExample.StackDemo();
        int result = stackDemo.recursiveMethod(10);
        assertEquals(55, result);
    }

    @Test
    void shouldDemonstrateStringPool() {
        MemoryModelExample.StringPoolDemo stringPool = new MemoryModelExample.StringPoolDemo();
        assertDoesNotThrow(stringPool::demonstrateStringInterning);
    }
}
