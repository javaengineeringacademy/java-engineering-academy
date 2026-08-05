import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstraction Tests")
class AbstractionTest {

    @Test
    @DisplayName("Cannot instantiate abstract class")
    void cannotInstantiate() {
        assertThrows(AbstractMethodError.class, () -> new Vehicle("X", "Y", 2020) {});
    }

    @Test
    @DisplayName("Car implements abstract methods")
    void carMethods() {
        Car car = new Car("Toyota", "Camry", 2023, 4);
        assertEquals(30.0, car.fuelEfficiency(), 0.001);
        assertNotNull(car.start());
        assertEquals(4, car.getDoors());
    }

    @Test
    @DisplayName("Motorcycle implements abstract methods")
    void motorcycleMethods() {
        Motorcycle bike = new Motorcycle("Honda", "CBR", 2023, false);
        assertEquals(50.0, bike.fuelEfficiency(), 0.001);
        assertNotNull(bike.start());
        assertFalse(bike.hasSidecar());
    }

    @Test
    @DisplayName("Polymorphism works with abstract class")
    void polymorphism() {
        Vehicle[] vehicles = {
            new Car("Toyota", "Camry", 2023, 4),
            new Motorcycle("Honda", "CBR", 2023, false)
        };
        for (Vehicle v : vehicles) {
            assertTrue(v.fuelEfficiency() > 0);
            assertNotNull(v.start());
        }
    }

    @Test
    @DisplayName("Inherited concrete methods work")
    void concreteMethods() {
        Car car = new Car("Toyota", "Camry", 2023, 4);
        assertTrue(car.getInfo().contains("2023"));
        assertTrue(car.getInfo().contains("Toyota"));
    }

    @Test
    @DisplayName("isNewerThan compares years")
    void isNewerThan() {
        Car old = new Car("Toyota", "Camry", 2020, 4);
        Car newer = new Car("Honda", "Civic", 2023, 4);
        assertTrue(newer.isNewerThan(old));
        assertFalse(old.isNewerThan(newer));
    }
}