package academy.javaengineering.oop.objects;

/**
 * Demonstrates object creation, reference variables, and memory model.
 *
 * <p>Objects are instances of classes allocated on the heap.
 * Reference variables store memory addresses pointing to heap objects.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Object instantiation with {@code new} keyword</li>
 *   <li>Reference variables vs objects</li>
 *   <li>Null references and NullPointerException</li>
 *   <li>Garbage collection basics</li>
 * </ul>
 */
public class ObjectDemo {

    private String data;

    public ObjectDemo(String data) {
        this.data = data;
    }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    /**
     * Demonstrates reference assignment (both vars point to same object).
     */
    public static ObjectDemo createSharedReference() {
        ObjectDemo original = new ObjectDemo("shared");
        ObjectDemo reference = original; // Both point to same object
        reference.setData("modified");
        return original; // original.data is now "modified"
    }

    /**
     * Demonstrates pass-by-value for primitives.
     */
    public static void modifyPrimitive(int value) {
        value = 100; // Does NOT affect the original
    }

    /**
     * Demonstrates pass-by-value for references (can modify object state).
     */
    public static void modifyObject(ObjectDemo obj) {
        obj.setData("modified by method"); // Affects the original object
    }

    public static void main(String[] args) {
        System.out.println("=== Object Creation and Memory Demo ===\n");

        // Object creation
        ObjectDemo obj1 = new ObjectDemo("first");
        ObjectDemo obj2 = new ObjectDemo("second");
        ObjectDemo obj3 = obj1; // obj3 points to same object as obj1

        System.out.println("obj1: " + obj1.getData());
        System.out.println("obj3 (same as obj1): " + obj3.getData());

        // Modifying through reference
        obj3.setData("modified");
        System.out.println("After modifying obj3, obj1: " + obj1.getData());

        // Null reference
        ObjectDemo nullObj = null;
        try {
            nullObj.getData(); // Throws NullPointerException
        } catch (NullPointerException e) {
            System.out.println("\nCaught NullPointerException for null reference");
        }

        // Primitive vs object pass-by-value
        int primitive = 42;
        modifyPrimitive(primitive);
        System.out.println("\nPrimitive after modify: " + primitive); // Still 42

        modifyObject(obj1);
        System.out.println("Object after modify: " + obj1.getData()); // Changed
    }
}
