package academy.javaengineering.jvm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassloaderTest {

    @Test
    void shouldPrintClassLoaderHierarchy() {
        ClassloaderExample.ClassLoaderHierarchy hierarchy = new ClassloaderExample.ClassLoaderHierarchy();
        assertDoesNotThrow(() -> hierarchy.printHierarchy(String.class));
    }

    @Test
    void shouldCreateCustomClassLoader() {
        ClassloaderExample.CustomClassLoader loader = new ClassloaderExample.CustomClassLoader();
        assertNotNull(loader);
        assertNotNull(loader.getParent());
    }

    @Test
    void shouldGetApplicationClassLoader() {
        ClassLoader loader = ClassloaderExample.class.getClassLoader();
        assertNotNull(loader);
    }
}
