import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CloneTest {

    @Test
    void testShallowClone() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        CloningEmployee original = new CloningEmployee("Alice", 101, 75000.0, address, "secret123");

        CloningEmployee shallowCopy = original.shallowClone();

        assertNotSame(original, shallowCopy);
        assertSame(original.getAddress(), shallowCopy.getAddress());
        assertEquals(original.getName(), shallowCopy.getName());
        assertEquals(original.getId(), shallowCopy.getId());
        assertEquals(original.getSalary(), shallowCopy.getSalary());

        shallowCopy.getAddress().setCity("Chicago");
        assertEquals("Chicago", original.getAddress().getCity());
    }

    @Test
    void testDeepClone() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        CloningEmployee original = new CloningEmployee("Bob", 102, 85000.0, address, "pass456");

        CloningEmployee deepCopy = original.deepClone();

        assertNotSame(original, deepCopy);
        assertNotSame(original.getAddress(), deepCopy.getAddress());
        assertEquals(original.getName(), deepCopy.getName());
        assertEquals(original.getId(), deepCopy.getId());
        assertEquals(original.getAddress().getCity(), deepCopy.getAddress().getCity());

        deepCopy.getAddress().setCity("Chicago");
        assertEquals("Springfield", original.getAddress().getCity());
    }

    @Test
    void testTransientField() {
        Address address = new Address("456 Oak Ave", "Metropolis", "NY", "10001");
        CloningEmployee original = new CloningEmployee("Charlie", 103, 95000.0, address, "mySecretPassword");

        CloningEmployee clone = original.deepClone();

        assertNull(clone.getPassword());
        assertNotNull(original.getPassword());
        assertEquals("mySecretPassword", original.getPassword());
    }

    @Test
    void testCopyConstructor() {
        Address address = new Address("789 Elm St", "Gotham", "NJ", "07001");
        ShallowVsDeepClone.Person original = new ShallowVsDeepClone.Person("Diana", address);

        ShallowVsDeepClone.Person copy = new ShallowVsDeepClone.Person(original);

        assertNotSame(original, copy);
        assertNotSame(original.getAddress(), copy.getAddress());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getAddress().getCity(), copy.getAddress().getCity());

        copy.getAddress().setCity("Star City");
        assertEquals("Gotham", original.getAddress().getCity());
    }

    @Test
    void testCopyMethod() {
        Address location = new Address("100 Industrial Blvd", "Central City", "CA", "90210");
        ShallowVsDeepClone.Vehicle original = new ShallowVsDeepClone.Vehicle("Tesla Model 3", location);

        ShallowVsDeepClone.Vehicle copy = original.copy();

        assertNotSame(original, copy);
        assertNotSame(original.getLocation(), copy.getLocation());
        assertEquals(original.getModel(), copy.getModel());
        assertEquals(original.getLocation().getCity(), copy.getLocation().getCity());

        copy.getLocation().setCity("Coast City");
        assertEquals("Central City", original.getLocation().getCity());
    }

    @Test
    void testAddressClone() {
        Address original = new Address("123 Main St", "Springfield", "IL", "62704");
        Address clone = original.clone();

        assertNotSame(original, clone);
        assertEquals(original.getStreet(), clone.getStreet());
        assertEquals(original.getCity(), clone.getCity());
        assertEquals(original.getState(), clone.getState());
        assertEquals(original.getZip(), clone.getZip());
    }

    @Test
    void testMultipleClonesAreIndependent() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        CloningEmployee original = new CloningEmployee("Eve", 104, 100000.0, address, "pwd123");

        CloningEmployee clone1 = original.deepClone();
        CloningEmployee clone2 = original.deepClone();

        assertNotSame(clone1, clone2);
        assertNotSame(clone1.getAddress(), clone2.getAddress());

        clone1.getAddress().setCity("City1");
        clone2.getAddress().setCity("City2");

        assertEquals("Springfield", original.getAddress().getCity());
        assertEquals("City1", clone1.getAddress().getCity());
        assertEquals("City2", clone2.getAddress().getCity());
    }

    @Test
    void testShallowCloneSharesAllMutableFields() {
        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        CloningEmployee original = new CloningEmployee("Frank", 105, 60000.0, address, "secret");

        CloningEmployee clone = original.shallowClone();

        clone.setSalary(99999.0);
        assertEquals(99999.0, original.getSalary());
    }
}
