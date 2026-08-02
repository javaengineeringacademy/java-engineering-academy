package academy.javaengineering.patterns.adapter;

public class AdapterExample {

    public interface TemperatureConverter {
        double convert(double temperature);
    }

    public static class FahrenheitSensor {
        public double readFahrenheit() {
            return 98.6;
        }
    }

    public static class FahrenheitToCelsiusAdapter implements TemperatureConverter {
        private final FahrenheitSensor sensor;

        public FahrenheitToCelsiusAdapter(FahrenheitSensor sensor) {
            this.sensor = sensor;
        }

        @Override
        public double convert(double temperature) {
            return (sensor.readFahrenheit() - 32) * 5 / 9;
        }
    }

    public static void main(String[] args) {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        double celsius = converter.convert(0);
        System.out.println("Temperature: " + celsius + "°C");
    }
}
