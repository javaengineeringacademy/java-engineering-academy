package academy.javaengineering.patterns.structural.proxy;

public class ProxyExample {
    public static void main(String[] args) {
        Image image1 = new ProxyImage("photo1.jpg");
        Image image2 = new ProxyImage("photo2.jpg");

        System.out.println("First call to image1:");
        image1.display();
        System.out.println("Second call to image1 (cached):");
        image1.display();

        System.out.println("First call to image2:");
        image2.display();
    }
}
