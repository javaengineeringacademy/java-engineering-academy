package academy.javaengineering.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class Directory extends FileSystemItem {
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    public void remove(FileSystemItem item) {
        children.remove(item);
    }

    public FileSystemItem getChild(int index) {
        return children.get(index);
    }

    @Override
    public int getSize() {
        return children.stream().mapToInt(FileSystemItem::getSize).sum();
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "Directory: " + name + " (" + getSize() + " bytes)");
        for (FileSystemItem item : children) {
            item.print(prefix + "  ");
        }
    }
}
