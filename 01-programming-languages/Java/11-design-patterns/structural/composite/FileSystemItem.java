package academy.javaengineering.patterns.structural.composite;

public abstract class FileSystemItem {
    protected String name;

    public FileSystemItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getSize();
    public abstract void print(String prefix);
}
