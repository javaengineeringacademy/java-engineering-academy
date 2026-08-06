public class GenericIntro {

    public static void main(String[] args) {
        withoutGenerics();
        withGenerics();
        genericMethodDemo();
    }

    @SuppressWarnings("rawtypes") // Intentional: demonstrates the problem with raw types
    public static void withoutGenerics() {
        System.out.println("=== Without Generics ===");
        java.util.List list = new java.util.ArrayList();
        list.add("Hello");
        list.add(42);
        list.add(true);

        for (Object item : list) {
            System.out.println("Item: " + item + " (Type: " + item.getClass().getSimpleName() + ")");
        }
    }

    public static void withGenerics() {
        System.out.println("\n=== With Generics ===");
        java.util.List<String> stringList = new java.util.ArrayList<>();
        stringList.add("Hello");
        stringList.add("World");

        for (String item : stringList) {
            System.out.println("String: " + item.toUpperCase());
        }
    }

    public static void genericMethodDemo() {
        System.out.println("\n=== Generic Method Demo ===");
        Integer intResult = maximum(10, 20, 30);
        System.out.println("Max integer: " + intResult);

        String strResult = maximum("Apple", "Banana", "Cherry");
        System.out.println("Max string: " + strResult);
    }

    public static <T extends Comparable<T>> T maximum(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) {
            max = b;
        }
        if (c.compareTo(max) > 0) {
            max = c;
        }
        return max;
    }

    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    public static <T> int countOccurrences(T[] array, T target) {
        int count = 0;
        for (T element : array) {
            if (element.equals(target)) {
                count++;
            }
        }
        return count;
    }
}
