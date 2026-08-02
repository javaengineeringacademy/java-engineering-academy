package academy.javaengineering.testing;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 Example - Annotations, Assertions, Lifecycle.
 */
public class JUnit5Example {

    private final List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public List<String> getItems() {
        return List.copyOf(items);
    }

    public int getSize() {
        return items.size();
    }

    public boolean contains(String item) {
        return items.contains(item);
    }

    public static void main(String[] args) {
        JUnit5Example example = new JUnit5Example();
        example.addItem("Item1");
        example.addItem("Item2");
        System.out.println("Items: " + example.getItems());
        System.out.println("Size: " + example.getSize());
    }
}
