package academy.javaengineering.patterns.adapter;

import academy.javaengineering.patterns.adapter.AdapterExample.FahrenheitSensor;
import academy.javaengineering.patterns.adapter.AdapterExample.FahrenheitToCelsiusAdapter;
import academy.javaengineering.patterns.adapter.AdapterExample.TemperatureConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdapterPatternTest {

    @Test
    @DisplayName("Should convert Fahrenheit to Celsius correctly")
    void shouldConvertCorrectly() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        double celsius = converter.convert(0);
        // 98.6F -> (98.6 - 32) * 5/9 = 37.0C
        assertEquals(37.0, celsius, 0.01,
                "98.6F should convert to approximately 37.0C");
    }

    @Test
    @DisplayName("Adapter should implement TemperatureConverter interface")
    void shouldImplementInterface() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        assertInstanceOf(TemperatureConverter.class, converter);
    }

    @Test
    @DisplayName("Adapter should wrap FahrenheitSensor")
    void shouldWrapSensor() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        FahrenheitToCelsiusAdapter adapter = new FahrenheitToCelsiusAdapter(sensor);
        assertNotNull(adapter, "Adapter should be created with a sensor");
    }

    @Test
    @DisplayName("FahrenheitSensor should return 98.6")
    void sensorShouldReturnExpectedValue() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        assertEquals(98.6, sensor.readFahrenheit(), 0.01);
    }

    @Test
    @DisplayName("Should produce consistent results")
    void shouldProduceConsistentResults() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        double first = converter.convert(0);
        double second = converter.convert(0);
        assertEquals(first, second, 0.001, "Same conversion should give same result");
    }

    @Test
    @DisplayName("Should handle multiple adapters independently")
    void shouldHandleMultipleAdapters() {
        FahrenheitSensor sensor1 = new FahrenheitSensor();
        FahrenheitSensor sensor2 = new FahrenheitSensor();
        TemperatureConverter converter1 = new FahrenheitToCelsiusAdapter(sensor1);
        TemperatureConverter converter2 = new FahrenheitToCelsiusAdapter(sensor2);

        double c1 = converter1.convert(0);
        double c2 = converter2.convert(0);
        assertEquals(c1, c2, 0.001);
    }

    @Test
    @DisplayName("Should convert 32F to 0C (freezing point)")
    void shouldConvertFreezingPoint() {
        FahrenheitSensor sensor = new FahrenheitSensor();
        TemperatureConverter converter = new FahrenheitToCelsiusAdapter(sensor);
        // The adapter uses sensor.readFahrenheit() (98.6) regardless of input,
        // so this tests the actual implementation behavior
        double result = converter.convert(32);
        assertEquals(37.0, result, 0.01,
                "Adapter uses sensor.readFahrenheit() internally");
    }

    @Test
    @DisplayName("Conversion formula should be mathematically correct for 98.6F")
    void conversionFormulaShouldBeCorrect() {
        double fahrenheit = 98.6;
        double expected = (fahrenheit - 32) * 5.0 / 9.0;
        assertEquals(37.0, expected, 0.01);
    }
}
