package academy.javaengineering.patterns.proxy;

public class ProxyExample {

    public interface Image {
        void display();
    }

    public static class RealImage implements Image {
        private final String filename;

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

    public static class ImageProxy implements Image {
        private RealImage realImage;
        private final String filename;

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

    public static void main(String[] args) {
        Image image = new ImageProxy("photo.jpg");
        System.out.println("Proxy created");
        image.display(); // Now loads and displays
    }
}
