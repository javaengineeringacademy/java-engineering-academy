package academy.javaengineering.patterns.behavioral.memento;

/**
 * Originator class - The object whose state is being saved.
 * Creates mementos and restores from them.
 */
public class Editor {

    private String content;
    private String title;

    public void write(String content) {
        this.content = content;
        System.out.println("Editor: Writing content...");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Memento save() {
        System.out.println("Editor: Saving state...");
        return new Memento(content);
    }

    public void restore(Memento memento) {
        this.content = memento.getContent();
        System.out.println("Editor: Restoring state...");
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Editor{content='" + content + "'}";
    }
}
