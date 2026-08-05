package academy.javaengineering.patterns.behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Collection implementation - Book Collection.
 * Manages a collection of books and provides an iterator.
 */
public class BookCollection implements Iterable<String> {

    private final List<String> books = new ArrayList<>();

    public void addBook(String book) {
        books.add(book);
    }

    public void removeBook(String book) {
        books.remove(book);
    }

    public String getBook(int index) {
        return books.get(index);
    }

    @Override
    public Iterator<String> createIterator() {
        return new BookIterator(this);
    }

    @Override
    public int size() {
        return books.size();
    }

    public List<String> getBooks() {
        return new ArrayList<>(books);
    }
}
