package academy.javaengineering.patterns.proxy;

/**
 * Demonstrates the Proxy design pattern for lazy loading and access control.
 *
 * <p>The Proxy pattern provides a surrogate or placeholder for another object to
 * control access to it. This example shows a virtual proxy for lazy initialization.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Subject interface shared by real object and proxy</li>
 *   <li>Virtual proxy for lazy loading</li>
 *   <li>Transparent access to underlying resource</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ProxyExample {

    /**
     * Subject interface for image operations.
     */
    public interface Image {
        /**
         * Displays the image.
         */
        void display();
    }

    /**
     * Real image implementation that loads from disk.
     */
    public static class RealImage implements Image {
        private final String filename;

        /**
         * Creates a real image and loads it from disk.
         *
         * @param filename the image file name
         */
        public RealImage(String filename) {
            this.filename = filename;
            loadFromDisk();
        }

        private void loadFromDisk() {
            System.out.println("Loading " + filename + " from disk...");
        }

        @Override
        public void display() {
            System.out.println("Displaying " + filename);
        }
    }

    /**
     * Proxy image that lazily loads the real image on first use.
     */
    public static class ImageProxy implements Image {
        private RealImage realImage;
        private final String filename;

        /**
         * Creates a proxy for the specified image.
         *
         * @param filename the image file name
         */
        public ImageProxy(String filename) {
            this.filename = filename;
        }

        @Override
        public void display() {
            if (realImage == null) {
                realImage = new RealImage(filename);
            }
            realImage.display();
        }
    }

    /**
     * Demonstrates proxy pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Image image = new ImageProxy("photo.jpg");
        System.out.println("Proxy created");
        image.display(); // Now loads and displays
    }
}
