package academy.javaengineering.jvm.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Solution 2: Custom ClassLoader Implementation
 *
 * A directory-based classloader that loads .class files from a specified
 * directory on the filesystem, following the parent delegation model.
 */
public class Solution2 {

    public static void main(String[] args) {
        System.out.println("=== Custom ClassLoader Solution ===\n");

        System.out.println("--- CustomClassLoader Created ---");
        DirectoryClassLoaderSolution loader = new DirectoryClassLoaderSolution(
            ".", Solution2.class.getClassLoader());
        System.out.println("Custom classloader: " + loader);
        System.out.println("Parent: " + loader.getParent());
    }
}

class DirectoryClassLoaderSolution extends ClassLoader {

    private final Path basePath;

    public DirectoryClassLoaderSolution(String basePath, ClassLoader parent) {
        super(parent);
        this.basePath = Paths.get(basePath).toAbsolutePath();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 1. Convert class name to file path
        String fileName = name.replace('.', '/') + ".class";
        Path filePath = basePath.resolve(fileName);

        // 2. Check if file exists
        if (!Files.exists(filePath)) {
            throw new ClassNotFoundException("Class file not found: " + filePath);
        }

        try {
            // 3. Read bytes from file
            byte[] bytes = Files.readAllBytes(filePath);

            // 4. Call defineClass() to create the Class object
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class: " + name, e);
        }
    }
}
