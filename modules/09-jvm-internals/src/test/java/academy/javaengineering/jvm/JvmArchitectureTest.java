package academy.javaengineering.jvm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JvmArchitectureTest {

    @Test
    void shouldPrintClassLoaderInfo() {
        JvmArchitectureExample.ClassLoaderDemo demo = new JvmArchitectureExample.ClassLoaderDemo();
        assertDoesNotThrow(demo::printClassLoaderInfo);
    }

    @Test
    void shouldDemonstrateRuntimeDataAreas() {
        JvmArchitectureExample.RuntimeDataAreas runtimeData = new JvmArchitectureExample.RuntimeDataAreas();
        assertDoesNotThrow(() -> runtimeData.demonstrateStackFrame(40));
    }

    @Test
    void shouldGetMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        assertTrue(runtime.maxMemory() > 0);
        assertTrue(runtime.totalMemory() > 0);
    }
}
