package academy.javaengineering.patterns.behavioral.iterator;

/**
 * Real-world example demonstrating the Iterator pattern.
 * Shows iterating through a book collection.
 */
public class IteratorExample {

    public static void main(String[] args) {
        BookCollection library = new BookCollection();
        library.addBook("Design Patterns");
        library.addBook("Clean Code");
        library.addBook("Effective Java");
        library.addBook("Head First Design Patterns");

        System.out.println("=== Iterating Through Books ===");
        Iterator<String> iterator = library.createIterator();
        while (iterator.hasNext()) {
            System.out.println("Book: " + iterator.next());
        }

        System.out.println("\n=== Using For-Each Style ===");
        Iterator<String> iter = library.createIterator();
        while (iter.hasNext()) {
            System.out.println("- " + iter.next());
        }

        System.out.println("\n=== Collection Size ===");
        System.out.println("Total books: " + library.size());
    }
}
