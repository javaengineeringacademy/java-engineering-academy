package academy.javaengineering.generics.internals;

public class GenericTypesInternals {

    static class Box<T> {
        private T value;

        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
    }

    static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Types Internals ===\n");

        // 1. Type Erasure at Compile Time
        System.out.println("--- Type Erasure ---");
        Box<String> stringBox = new Box<>("Java");
        Box<Integer> intBox = new Box<>(42);
        System.out.println("Box<String> and Box<Integer> become Box at runtime");
        System.out.println("Type parameter T replaced with Object (or bound)");

        // 2. Bytecode Verification
        System.out.println("\n--- Bytecode ---");
        System.out.println("Generic types checked at compile time only");
        System.out.println("At runtime: Box<String> == Box<Integer>");
        System.out.println("javap shows: Box.value is Object type");

        // 3. Multiple Type Parameters
        System.out.println("\n--- Multiple Type Parameters ---");
        Pair<String, Integer> pair = new Pair<>("age", 25);
        System.out.println("Pair<K,V> allows two independent types");
        System.out.println("K=" + pair.getKey() + ", V=" + pair.getValue());

        // 4. Diamond Operator <>
        System.out.println("\n--- Diamond Operator ---");
        Box<String> diamond = new Box<>("Inferred");
        System.out.println("Java 7+: new Box<>(\"Inferred\")");
        System.out.println("Type inferred from declaration");

        // 5. Nested Generics
        System.out.println("\n--- Nested Generics ---");
        Box<Pair<String, Integer>> nested = new Box<>(pair);
        System.out.println("Box<Pair<K,V>> - generics compose");
        System.out.println("Inner type: " + nested.getValue().getKey());
    }
}
