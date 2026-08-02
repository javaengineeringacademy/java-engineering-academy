package academy.javaengineering.cheatsheets;

/**
 * Demonstrates Java cheat sheets.
 */
public class JavaCheatSheet {

    public static java.util.Map<String, String> getSyntaxQuickReference() {
        return java.util.Map.of(
            "Variable", "type name = value;",
            "Method", "returnType methodName(params) { }",
            "Class", "class ClassName { }",
            "Interface", "interface InterfaceName { }",
            "If", "if (condition) { } else { }",
            "For", "for (int i = 0; i < n; i++) { }",
            "While", "while (condition) { }",
            "Switch", "switch (value) { case 1: break; }"
        );
    }

    public static java.util.Map<String, String> getCollectionsQuickReference() {
        return java.util.Map.of(
            "ArrayList", "Dynamic array, O(1) access",
            "LinkedList", "Doubly linked list, O(1) insert/delete",
            "HashMap", "Hash table, O(1) get/put",
            "TreeMap", "Red-black tree, O(log n) get/put",
            "HashSet", "Hash table, O(1) add/remove",
            "TreeSet", "Red-black tree, O(log n) add/remove"
        );
    }
}
