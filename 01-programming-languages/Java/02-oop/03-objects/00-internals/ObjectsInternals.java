package academy.javaengineering.oop.internals;

public class ObjectsInternals {

    static class Car {
        String model;
        int year;

        Car(String model, int year) {
            this.model = model;
            this.year = year;
        }

        void display() {
            System.out.println(year + " " + model);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Objects Internals ===\n");

        // 1. Object Creation
        System.out.println("--- Object Creation ---");
        Car car1 = new Car("Toyota", 2024);
        Car car2 = new Car("Honda", 2023);
        System.out.println("new keyword: allocates memory + calls constructor");
        System.out.println("car1: " + car1);
        System.out.println("car2: " + car2);

        // 2. Reference vs Object
        System.out.println("\n--- Reference vs Object ---");
        System.out.println("Reference: pointer to object (8 bytes)");
        System.out.println("Object: actual data in heap");
        System.out.println("Multiple references can point to same object");

        // 3. Object Identity
        System.out.println("\n--- Object Identity ---");
        System.out.println("== : compares references");
        System.out.println(".equals(): compares content");
        System.out.println("car1 == car2: " + (car1 == car2));
        System.out.println("car1.equals(car2): " + car1.equals(car2));

        // 4. Garbage Collection
        System.out.println("\n--- Garbage Collection ---");
        System.out.println("Objects with no references are eligible");
        System.out.println("GC reclaims heap memory automatically");
        System.out.println("System.gc() suggests GC (not guaranteed)");
    }
}
