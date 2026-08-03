package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class JUnit5ExampleTest {

    private JUnit5Example example;

    @BeforeEach
    void setUp() {
        example = new JUnit5Example();
    }

    @Test
    void shouldAddItem() {
        example.addItem("Item1");
        assertEquals(1, example.getSize());
        assertTrue(example.contains("Item1"));
    }

    @Test
    void shouldRemoveItem() {
        example.addItem("Item1");
        example.removeItem("Item1");
        assertEquals(0, example.getSize());
    }

    @Test
    void shouldReturnCopyOfItems() {
        example.addItem("Item1");
        example.addItem("Item2");
        assertEquals(2, example.getItems().size());
    }

    @Test
    void shouldReturnCorrectSize() {
        assertEquals(0, example.getSize());
        example.addItem("Item1");
        assertEquals(1, example.getSize());
    }
}
