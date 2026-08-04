package academy.javaengineering.patterns.composite;

import java.util.ArrayList;
import java.util.List;

// Component
interface FileSystemComponent {
    String getName();
    int getSize();
    void print(String prefix);
}

// Leaf
class File implements FileSystemComponent {
    private final String name;
    private final int size;
    
    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public int getSize() { return size; }
    
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "📄 " + name + " (" + size + "KB)");
    }
}

// Composite
class Directory implements FileSystemComponent {
    private final String name;
    private final List<FileSystemComponent> children = new ArrayList<>();
    
    public Directory(String name) {
        this.name = name;
    }
    
    public void add(FileSystemComponent component) {
        children.add(component);
    }
    
    public void remove(FileSystemComponent component) {
        children.remove(component);
    }
    
    public FileSystemComponent getChild(int index) {
        return children.get(index);
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public int getSize() {
        return children.stream().mapToInt(FileSystemComponent::getSize).sum();
    }
    
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "📁 " + name + " (" + getSize() + "KB)");
        children.forEach(child -> child.print(prefix + "  "));
    }
}

public class CompositeExample {
    public static void main(String[] args) {
        System.out.println("=== Composite Pattern ===\n");
        
        Directory root = new Directory("root");
        Directory src = new Directory("src");
        Directory main = new Directory("main");
        Directory test = new Directory("test");
        
        main.add(new File("App.java", 50));
        main.add(new File("Utils.java", 30));
        test.add(new File("AppTest.java", 40));
        
        src.add(main);
        src.add(test);
        root.add(src);
        root.add(new File("README.md", 10));
        root.add(new File("pom.xml", 5));
        
        root.print("");
        
        System.out.println("\nTotal size: " + root.getSize() + "KB");
    }
}
