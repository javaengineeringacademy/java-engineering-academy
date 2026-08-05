package academy.javaengineering.patterns.structural.composite;

public class File extends FileSystemItem {
    private final int size;

    public File(String name, int size) {
        super(name);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "File: " + name + " (" + size + " bytes)");
    }
}
