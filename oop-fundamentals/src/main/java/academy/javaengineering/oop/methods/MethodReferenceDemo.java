package academy.javaengineering.oop.methods;

/**
 * Demonstrates method references.
 */
public final class MethodReferenceDemo {

    public static void main(String[] args) {
        // Static method reference
        java.util.function.Function<String, Integer> parser = Integer::parseInt;
        System.out.println("parseInt: " + parser.apply("42"));

        // Instance method reference
        java.util.function.Consumer<String> printer = System.out::println;
        printer.accept("Hello from method reference!");

        // Constructor reference
        java.util.function.Supplier<java.util.ArrayList<String>> listMaker = java.util.ArrayList::new;
        var list = listMaker.get();
        list.add("Hello");
        System.out.println("List: " + list);

        // Instance method of arbitrary object
        String str = "hello";
        java.util.function.Function<String, String> upper = String::toUpperCase;
        System.out.println("Upper: " + upper.apply(str));
    }
}