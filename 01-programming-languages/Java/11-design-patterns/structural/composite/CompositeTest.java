package academy.javaengineering.patterns.structural.composite;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositeTest {

    @Test
    void testFileCreation() {
        File file = new File("test.txt", 100);
        assertEquals("test.txt", file.getName());
        assertEquals(100, file.getSize());
    }

    @Test
    void testDirectoryCreation() {
        Directory dir = new Directory("testDir");
        assertEquals("testDir", dir.getName());
        assertEquals(0, dir.getSize());
    }

    @Test
    void testDirectoryAddChild() {
        Directory dir = new Directory("testDir");
        File file = new File("test.txt", 100);
        dir.add(file);
        assertEquals(100, dir.getSize());
    }

    @Test
    void testNestedDirectories() {
        Directory root = new Directory("root");
        Directory sub = new Directory("sub");
        File file = new File("file.txt", 50);
        sub.add(file);
        root.add(sub);
        assertEquals(50, root.getSize());
    }

    @Test
    void testMultipleFiles() {
        Directory dir = new Directory("dir");
        dir.add(new File("a.txt", 100));
        dir.add(new File("b.txt", 200));
        assertEquals(300, dir.getSize());
    }
}
