package academy.javaengineering.patterns.adapter;

/**
 * Demonstrates the Adapter design pattern for interface compatibility.
 *
 * <p>The Adapter pattern converts the interface of a class into another interface
 * clients expect. It allows classes with incompatible interfaces to work together.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Target interface clients use</li>
 *   <li>Adaptee class with incompatible interface</li>
 *   <li>Adapter converts adaptee interface to target</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class AdapterExample {

    /**
     * Target interface for temperature conversion.
     */
    public interface TemperatureConverter {
        /**
         * Converts temperature to Celsius.
         *
         * @param temperature the temperature value
         * @return the temperature in Celsius
         */
        double convert(double temperature);
    }

    /**
     * Adaptee class that reads temperature in Fahrenheit.
     */
    public static class FahrenheitSensor {
        /**
         * Reads temperature in Fahrenheit.
         *
         * @return temperature in Fahrenheit
         */
        public double readFahrenheit() {
            return 98.6;
        }
    }

    /**
     * Adapter converting Fahrenheit readings to Celsius.
     */
    public static class FahrenheitToCelsiusAdapter implements TemperatureConverter {
        private final FahrenheitSensor sensor;

        /**
         * Creates an adapter for the specified sensor.
         *
         * @param sensor the Fahrenheit sensor to adapt
         */
        public FahrenheitToCelsiusAdapter(FahrenheitSensor sensor) {
            this.sensor = sensor;
        }

        @Override
        public double convert(double temperature) {
            return (sensor.readFahrenheit() - 32) * 5 / 9;
        }
    }

    /**
     * Demonstrates adapter pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        double celsius = converter.convert(0);
        System.out.println("Temperature: " + celsius + "°C");
    }
}
