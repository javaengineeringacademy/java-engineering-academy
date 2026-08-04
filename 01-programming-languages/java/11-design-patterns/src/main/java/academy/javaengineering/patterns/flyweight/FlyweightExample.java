package academy.javaengineering.patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

// Flyweight
interface TreeType {
    void draw(int x, int y);
}

class PineTree implements TreeType {
    private final String name;
    private final String color;
    private final String texture;
    
    public PineTree(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
        System.out.println("Creating PineTree type: " + name);
    }
    
    @Override
    public void draw(int x, int y) {
        System.out.println("Drawing Pine '" + name + "' at (" + x + "," + y + ") with " + color + " color");
    }
}

class OakTree implements TreeType {
    private final String name;
    private final String color;
    private final String texture;
    
    public OakTree(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
        System.out.println("Creating OakTree type: " + name);
    }
    
    @Override
    public void draw(int x, int y) {
        System.out.println("Drawing Oak '" + name + "' at (" + x + "," + y + ") with " + color + " color");
    }
}

// Flyweight Factory
class TreeFactory {
    private static final Map<String, TreeType> treeTypes = new HashMap<>();
    
    public static TreeType getTreeType(String type, String name, String color, String texture) {
        String key = type + "-" + name + "-" + color + "-" + texture;
        
        if (!treeTypes.containsKey(key)) {
            treeTypes.put(key, switch (type.toLowerCase()) {
                case "pine" -> new PineTree(name, color, texture);
                case "oak" -> new OakTree(name, color, texture);
                default -> throw new IllegalArgumentException("Unknown tree type: " + type);
            });
            System.out.println("Tree type created. Total types: " + treeTypes.size());
        }
        
        return treeTypes.get(key);
    }
    
    public static int getTreeTypeCount() {
        return treeTypes.size();
    }
}

// Context
class Tree {
    private final int x;
    private final int y;
    private final TreeType type;
    
    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void draw() {
        type.draw(x, y);
    }
}

// Forest
class Forest {
    private final java.util.List<Tree> trees = new java.util.ArrayList<>();
    
    public void plantTree(int x, int y, String type, String name, String color, String texture) {
        TreeType treeType = TreeFactory.getTreeType(type, name, color, texture);
        trees.add(new Tree(x, y, treeType));
    }
    
    public void draw() {
        trees.forEach(Tree::draw);
    }
}

public class FlyweightExample {
    public static void main(String[] args) {
        System.out.println("=== Flyweight Pattern ===\n");
        
        Forest forest = new Forest();
        
        // Plant many trees with shared types
        for (int i = 0; i < 5; i++) {
            forest.plantTree(i * 10, i * 5, "pine", "Evergreen", "Dark Green", "rough");
            forest.plantTree(i * 10 + 5, i * 5 + 3, "oak", "Royal Oak", "Brown", "smooth");
        }
        
        System.out.println("\n--- Drawing Forest ---");
        forest.draw();
        
        System.out.println("\nTree types created: " + TreeFactory.getTreeTypeCount());
        System.out.println("Trees planted: 10 (but only 2 unique types)");
    }
}
