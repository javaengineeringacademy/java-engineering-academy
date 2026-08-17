package academy.javaengineering.collections.map.hashtable.examples;

import java.util.*;

public class HashtableExample {
    public static void main(String[] args) {
        System.out.println("=== Hashtable Examples ===\n");
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        table.put("Python", 2);
        System.out.println("Table: " + table);
        System.out.println("Get Java: " + table.get("Java"));
    }
}
