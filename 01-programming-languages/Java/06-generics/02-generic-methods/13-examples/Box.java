package academy.javaengineering.generics.generic-types.examples;

import java.util.Objects;

/**
 * A generic Box class that can hold any type of content.
 *
 * <p>Complexity: O(1) for all operations</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Generic container with equals, hashCode, and isEmpty support</p>
 */
public class Box<T> {
    private T content;

    public Box() {
    }

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return content == null;
    }

    @Override
    public String toString() {
        return "Box{content=" + content + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Box<?> other = (Box<?>) obj;
        return Objects.equals(content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
