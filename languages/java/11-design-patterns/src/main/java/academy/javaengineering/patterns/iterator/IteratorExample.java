package academy.javaengineering.patterns.iterator;

import java.util.ArrayList;
import java.util.List;

// Iterator Interface
interface Iterator<T> {
    boolean hasNext();
    T next();
    void reset();
}

// Aggregate Interface
interface IterableCollection<T> {
    Iterator<T> createIterator();
}

// Concrete Collection
class BookCollection implements IterableCollection<String> {
    private final List<String> books = new ArrayList<>();
    
    public void addBook(String book) {
        books.add(book);
    }
    
    public int size() {
        return books.size();
    }
    
    public String get(int index) {
        return books.get(index);
    }
    
    @Override
    public Iterator<String> createIterator() {
        return new BookIterator(this);
    }
    
    // Forward Iterator
    private static class BookIterator implements Iterator<String> {
        private final BookCollection collection;
        private int position = 0;
        
        BookIterator(BookCollection collection) {
            this.collection = collection;
        }
        
        @Override
        public boolean hasNext() {
            return position < collection.size();
        }
        
        @Override
        public String next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return collection.get(position++);
        }
        
        @Override
        public void reset() {
            position = 0;
        }
    }
}

// Reverse Iterator
class ReverseBookIterator implements Iterator<String> {
    private final BookCollection collection;
    private int position;
    
    ReverseBookIterator(BookCollection collection) {
        this.collection = collection;
        this.position = collection.size() - 1;
    }
    
    @Override
    public boolean hasNext() {
        return position >= 0;
    }
    
    @Override
    public String next() {
        if (!hasNext()) throw new java.util.NoSuchElementException();
        return collection.get(position--);
    }
    
    @Override
    public void reset() {
        position = collection.size() - 1;
    }
}

// Filter Iterator
class FilterIterator implements Iterator<String> {
    private final Iterator<String> iterator;
    private final java.util.function.Predicate<String> filter;
    private String nextItem;
    private boolean hasNextItem;
    
    FilterIterator(Iterator<String> iterator, java.util.function.Predicate<String> filter) {
        this.iterator = iterator;
        this.filter = filter;
        advanceToNext();
    }
    
    private void advanceToNext() {
        hasNextItem = false;
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (filter.test(item)) {
                nextItem = item;
                hasNextItem = true;
                break;
            }
        }
    }
    
    @Override
    public boolean hasNext() {
        return hasNextItem;
    }
    
    @Override
    public String next() {
        if (!hasNextItem) throw new java.util.NoSuchElementException();
        String result = nextItem;
        advanceToNext();
        return result;
    }
    
    @Override
    public void reset() {
        iterator.reset();
        advanceToNext();
    }
}

public class IteratorExample {
    public static void main(String[] args) {
        System.out.println("=== Iterator Pattern ===\n");
        
        BookCollection books = new BookCollection();
        books.addBook("Design Patterns");
        books.addBook("Clean Code");
        books.addBook("Effective Java");
        books.addBook("Refactoring");
        books.addBook("The Pragmatic Programmer");
        
        System.out.println("--- Forward Iterator ---");
        Iterator<String> forward = books.createIterator();
        while (forward.hasNext()) {
            System.out.println("Book: " + forward.next());
        }
        
        System.out.println("\n--- Reverse Iterator ---");
        Iterator<String> reverse = new ReverseBookIterator(books);
        while (reverse.hasNext()) {
            System.out.println("Book: " + reverse.next());
        }
        
        System.out.println("\n--- Filter Iterator (containing 'Java') ---");
        Iterator<String> filtered = new FilterIterator(books.createIterator(), b -> b.contains("Java"));
        while (filtered.hasNext()) {
            System.out.println("Book: " + filtered.next());
        }
    }
}
