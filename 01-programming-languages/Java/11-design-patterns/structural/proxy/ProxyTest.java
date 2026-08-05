package academy.javaengineering.patterns.structural.proxy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {

    @Test
    void testProxyImageCreation() {
        Image image = new ProxyImage("test.jpg");
        assertNotNull(image);
    }

    @Test
    void testRealImageCreation() {
        Image image = new RealImage("test.jpg");
        assertNotNull(image);
        assertEquals("test.jpg", image.getFileName());
    }

    @Test
    void testProxyFileName() {
        Image image = new ProxyImage("proxy.jpg");
        assertEquals("proxy.jpg", image.getFileName());
    }

    @Test
    void testRealImageFileName() {
        Image image = new RealImage("real.jpg");
        assertEquals("real.jpg", image.getFileName());
    }

    @Test
    void testProxyInterface() {
        Image image = new ProxyImage("test.jpg");
        assertTrue(image instanceof Image);
    }
}
