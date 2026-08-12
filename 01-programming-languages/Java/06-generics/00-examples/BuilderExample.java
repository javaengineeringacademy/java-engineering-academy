package academy.javaengineering.generics.examples;

/**
 * Generic Builder Pattern example.
 *
 * <p>This example demonstrates a type-safe builder pattern using generics,
 * allowing fluent API construction with compile-time type checking.</p>
 */
public class BuilderExample {

    /**
     * Generic builder class.
     *
     * @param <T> the type being built
     */
    public static class Builder<T> {
        private T value;

        public Builder<T> with(T value) {
            this.value = value;
            return this;
        }

        public T build() {
            return value;
        }
    }

    /**
     * Person class built using the generic builder.
     */
    public static class Person {
        private final String name;
        private final int age;

        private Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    public static void main(String[] args) {
        Builder<String> stringBuilder = new Builder<>();
        stringBuilder.with("Hello");
        String result = stringBuilder.build();
        System.out.println("String result: " + result);

        Person person = new Person("Alice", 30);
        System.out.println("Person: " + person);
    }
}
