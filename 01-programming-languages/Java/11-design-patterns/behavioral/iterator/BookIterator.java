package academy.javaengineering.patterns.behavioral.iterator;

/**
 * Concrete Iterator implementation - Book Iterator.
 * Traverses the BookCollection sequentially.
 */
public class BookIterator implements Iterator<String> {

    private final BookCollection collection;
    private int position = 0;

    public BookIterator(BookCollection collection) {
        this.collection = collection;
    }

    @Override
    public boolean hasNext() {
        return position < collection.size();
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException("No more elements");
        }
        return collection.getBook(position++);
    }

    @Override
    public void reset() {
        position = 0;
    }

    public int getPosition() {
        return position;
    }
}
