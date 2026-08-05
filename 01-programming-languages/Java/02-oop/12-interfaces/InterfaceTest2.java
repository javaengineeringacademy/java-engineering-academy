import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Interface Tests")
class InterfaceTest {

    @Test
    @DisplayName("Book implements Printable correctly")
    void bookImplementsPrintable() {
        Book book = new Book("Java", "Author", 300);
        assertTrue(book instanceof Printable);
    }

    @Test
    @DisplayName("format() returns formatted string")
    void format() {
        Book book = new Book("Java", "Author", 300);
        String result = book.format();
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("Author"));
        assertTrue(result.contains("300"));
    }

    @Test
    @DisplayName("Default method works")
    void defaultMethod() {
        Book book = new Book("Java", "Author", 300);
        String result = book.printWithHeader();
        assertTrue(result.contains("DOCUMENT"));
        assertTrue(result.contains("Java"));
    }

    @Test
    @DisplayName("Static interface method works")
    void staticMethod() {
        assertEquals("1.0", Printable.version());
    }

    @Test
    @DisplayName("Polymorphism via interface")
    void polymorphism() {
        Printable printable = new Book("Java", "Author", 300);
        assertNotNull(printable.format());
    }

    @Test
    @DisplayName("Book getters work")
    void bookGetters() {
        Book book = new Book("Java", "Author", 300);
        assertEquals("Java", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals(300, book.getPages());
    }
}