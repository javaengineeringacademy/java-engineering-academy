package academy.javaengineering.oop;

import academy.javaengineering.oop.composition.Car;
import academy.javaengineering.oop.composition.Engine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Composition Tests")
class CompositionTest {

    private Car car;

    @BeforeEach
    void setUp() {
        Engine engine = new Engine("V6", 300);
        car = new Car("Toyota", "Camry", 2023, engine);
    }

    @Test
    @DisplayName("Car contains engine")
    void carHasEngine() {
        assertNotNull(car.getEngine());
        assertEquals("V6", car.getEngine().getType());
        assertEquals(300, car.getEngine().getHorsepower());
    }

    @Test
    @DisplayName("Start car starts engine")
    void startCar() {
        assertFalse(car.getEngine().isRunning());
        car.start();
        assertTrue(car.getEngine().isRunning());
    }

    @Test
    @DisplayName("Stop car stops engine")
    void stopCar() {
        car.start();
        car.stop();
        assertFalse(car.getEngine().isRunning());
    }

    @Test
    @DisplayName("GetInfo includes car and engine details")
    void getInfo() {
        String info = car.getInfo();
        assertTrue(info.contains("Toyota"));
        assertTrue(info.contains("Camry"));
        assertTrue(info.contains("V6"));
    }

    @Test
    @DisplayName("Car fields are immutable")
    void immutability() {
        assertEquals("Toyota", car.getMake());
        assertEquals("Camry", car.getModel());
        assertEquals(2023, car.getYear());
    }

    @Test
    @DisplayName("Engine is independent object")
    void engineIndependence() {
        Engine engine = new Engine("V8", 400);
        Car car2 = new Car("Ford", "Mustang", 2023, engine);
        assertNotSame(car.getEngine(), car2.getEngine());
    }
}
