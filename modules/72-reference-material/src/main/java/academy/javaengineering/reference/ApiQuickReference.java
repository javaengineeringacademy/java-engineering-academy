package academy.javaengineering.reference;

/**
 * Demonstrates API quick reference.
 */
public class ApiQuickReference {

    public static java.util.Map<String, String> getStringMethods() {
        return java.util.Map.of(
            "length()", "Returns length of string",
            "charAt(int)", "Returns char at index",
            "substring(int)", "Returns substring from index",
            "toLowerCase()", "Converts to lowercase",
            "toUpperCase()", "Converts to uppercase",
            "trim()", "Removes whitespace",
            "split(String)", "Splits string",
            "contains(CharSequence)", "Checks if contains",
            "equals(Object)", "Compares strings"
        );
    }

    public static java.util.Map<String, String> getCollectionMethods() {
        return java.util.Map.of(
            "add(E)", "Adds element",
            "remove(Object)", "Removes element",
            "contains(Object)", "Checks if contains",
            "size()", "Returns size",
            "isEmpty()", "Checks if empty",
            "clear()", "Removes all elements",
            "iterator()", "Returns iterator",
            "toArray()", "Converts to array"
        );
    }
}
