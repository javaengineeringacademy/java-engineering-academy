package academy.javaengineering.oop.internals;

public class InnerClassesInternals {

    static class Outer {
        private String outerField = "Outer field";

        class Inner {
            void display() {
                System.out.println("Outer field: " + outerField);
            }
        }

        static class StaticInner {
            void display() {
                System.out.println("Static inner class");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Inner Classes Internals ===\n");

        // 1. Inner Class
        System.out.println("--- Inner Class ---");
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.display();
        System.out.println("Needs outer instance");

        // 2. Static Inner Class
        System.out.println("\n--- Static Inner Class ---");
        Outer.StaticInner staticInner = new Outer.StaticInner();
        staticInner.display();
        System.out.println("No outer instance needed");

        // 3. Local Class
        System.out.println("\n--- Local Class ---");
        class Local {
            void display() { System.out.println("Local class"); }
        }
        Local local = new Local();
        local.display();

        // 4. Anonymous Class
        System.out.println("\n--- Anonymous Class ---");
        Runnable r = new Runnable() {
            @Override
            public void run() { System.out.println("Anonymous class"); }
        };
        r.run();
    }
}
