public class Book implements Printable {

    private final String title;
    private final String author;
    private final int pages;

    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPages() { return pages; }

    @Override
    public String format() {
        return "Book: '%s' by %s (%d pages)".formatted(title, author, pages);
    }

    @Override
    public String toString() {
        return format();
    }
}