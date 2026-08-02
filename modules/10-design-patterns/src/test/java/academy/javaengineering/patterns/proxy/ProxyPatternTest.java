package academy.javaengineering.patterns.proxy;

import academy.javaengineering.patterns.proxy.ProxyExample.Image;
import academy.javaengineering.patterns.proxy.ProxyExample.ImageProxy;
import academy.javaengineering.patterns.proxy.ProxyExample.RealImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPatternTest {

    @Test
    @DisplayName("Proxy should implement Image interface")
    void proxyShouldImplementImageInterface() {
        Image proxy = new ImageProxy("test.jpg");
        assertInstanceOf(Image.class, proxy);
    }

    @Test
    @DisplayName("RealImage should implement Image interface")
    void realImageShouldImplementImageInterface() {
        Image real = new RealImage("test.jpg");
        assertInstanceOf(Image.class, real);
    }

    @Test
    @DisplayName("Proxy should not load image on construction")
    void proxyShouldNotLoadOnConstruction() {
        ImageProxy proxy = new ImageProxy("photo.jpg");
        assertNull(getRealImageField(proxy),
                "Proxy should not create RealImage until display() is called");
    }

    @Test
    @DisplayName("Proxy should load image on first display")
    void proxyShouldLoadOnFirstDisplay() {
        ImageProxy proxy = new ImageProxy("photo.jpg");
        proxy.display();
        assertNotNull(getRealImageField(proxy),
                "After display(), RealImage should be created");
    }

    @Test
    @DisplayName("Proxy should not reload on subsequent displays")
    void proxyShouldNotReloadOnSubsequentDisplay() {
        ImageProxy proxy = new ImageProxy("photo.jpg");
        proxy.display();
        Object first = getRealImageField(proxy);
        proxy.display();
        Object second = getRealImageField(proxy);
        assertSame(first, second,
                "Proxy should reuse the same RealImage instance");
    }

    @Test
    @DisplayName("Should not throw on display")
    void shouldNotThrowOnDisplay() {
        Image proxy = new ImageProxy("image.png");
        assertDoesNotThrow(proxy::display);
    }

    @Test
    @DisplayName("RealImage should not throw on display")
    void realImageShouldNotThrowOnDisplay() {
        Image real = new RealImage("image.png");
        assertDoesNotThrow(real::display);
    }

    @Test
    @DisplayName("Proxy and RealImage should both be usable through Image interface")
    void bothShouldBeUsableThroughInterface() {
        Image proxy = new ImageProxy("photo.jpg");
        Image real = new RealImage("photo.jpg");
        assertDoesNotThrow(proxy::display);
        assertDoesNotThrow(real::display);
    }

    private Object getRealImageField(ImageProxy proxy) {
        try {
            var field = ImageProxy.class.getDeclaredField("realImage");
            field.setAccessible(true);
            return field.get(proxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
