package academy.javaengineering.generics;

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
        return java.util.Objects.equals(content, other.content);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(content);
    }
}
