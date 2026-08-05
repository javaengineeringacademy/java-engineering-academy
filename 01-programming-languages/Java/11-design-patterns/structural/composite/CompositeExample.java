package academy.javaengineering.patterns.structural.composite;

public class CompositeExample {
    public static void main(String[] args) {
        File file1 = new File("readme.txt", 100);
        File file2 = new File("main.java", 500);
        File file3 = new File("style.css", 200);

        Directory srcDir = new Directory("src");
        srcDir.add(file2);

        Directory rootDir = new Directory("root");
        rootDir.add(file1);
        rootDir.add(srcDir);
        rootDir.add(file3);

        rootDir.print("");
        System.out.println("Total size: " + rootDir.getSize() + " bytes");
    }
}
