import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ThrowsDemo {

    public static void main(String[] args) {
        throwsDeclarationDemo();
        methodChainingDemo();
        throwsWithCheckedException();
    }

    public static void throwsDeclarationDemo() throws FileNotFoundException {
        System.out.println("=== Throws Declaration Demo ===");
        File file = new File("nonexistent.txt");
        Scanner scanner = new Scanner(file);
        System.out.println("File content: " + scanner.nextLine());
        scanner.close();
    }

    public static void methodChainingDemo() {
        System.out.println("\n=== Method Chaining Demo ===");
        try {
            processData("valid");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception propagated: " + e.getMessage());
        }
    }

    public static void throwsWithCheckedException() {
        System.out.println("\n=== Checked Exception Demo ===");
        try {
            readFile("data.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static void readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }

    public static void processData(String data) throws Exception {
        validateData(data);
        transformData(data);
    }

    private static void validateData(String data) throws IllegalArgumentException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data validation failed");
        }
    }

    private static void transformData(String data) throws IllegalArgumentException {
        if (data.length() < 3) {
            throw new IllegalArgumentException("Data too short for transformation");
        }
        System.out.println("Transformed: " + data.toUpperCase());
    }

    public static void multiExceptionDeclaration() throws 
            IllegalArgumentException, NullPointerException {
        String data = null;
        if (data == null) {
            throw new NullPointerException("Data is null");
        }
        data.isEmpty();
    }
}
