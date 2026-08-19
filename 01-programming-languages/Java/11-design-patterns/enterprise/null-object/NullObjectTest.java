package academy.javaengineering.patterns.enterprise.null_object;

/**
 * Tests for the Null Object pattern.
 */
public class NullObjectTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Null Object Pattern Tests ===\n");

        testDogSpeak();
        testDogIsReal();
        testDogLegs();
        testNullAnimalName();
        testNullAnimalSpeak();
        testNullAnimalIsReal();
        testNullAnimalLegs();
        testNullAnimalSingleton();
        testNullAnimalToString();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testDogSpeak() {
        Dog dog = new Dog("Rex");
        // speak() prints to stdout; just verify it doesn't throw
        dog.speak();
        assertTest("Dog speak no exception", true);
    }

    private static void testDogIsReal() {
        assertTest("Dog isReal", new Dog("Rex").isReal());
    }

    private static void testDogLegs() {
        assertTest("Dog legs = 4", new Dog("Rex").getLegs() == 4);
    }

    private static void testNullAnimalName() {
        assertTest("NullAnimal name", NullAnimal.getInstance().getName().equals("None"));
    }

    private static void testNullAnimalSpeak() {
        NullAnimal.getInstance().speak();
        assertTest("NullAnimal speak no exception", true);
    }

    private static void testNullAnimalIsReal() {
        assertTest("NullAnimal isReal false", !NullAnimal.getInstance().isReal());
    }

    private static void testNullAnimalLegs() {
        assertTest("NullAnimal legs = 0", NullAnimal.getInstance().getLegs() == 0);
    }

    private static void testNullAnimalSingleton() {
        assertTest("NullAnimal singleton",
                NullAnimal.getInstance() == NullAnimal.getInstance());
    }

    private static void testNullAnimalToString() {
        assertTest("NullAnimal toString",
                NullAnimal.getInstance().toString().equals("NullAnimal{}"));
    }

    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }
}
