package academy.javaengineering.patterns.creational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbstractFactoryTest {

    @Test
    void windowsFactoryCreatesWindowsButton() {
        AbstractFactory factory = new WindowsFactory();
        Button button = factory.createButton();
        assertInstanceOf(WindowsButton.class, button);
        assertEquals("windows", button.getStyle());
    }

    @Test
    void windowsFactoryCreatesWindowsTextBox() {
        AbstractFactory factory = new WindowsFactory();
        TextBox textBox = factory.createTextBox();
        assertInstanceOf(WindowsTextBox.class, textBox);
        assertEquals("windows", textBox.getStyle());
    }

    @Test
    void macFactoryCreatesMacButton() {
        AbstractFactory factory = new MacFactory();
        Button button = factory.createButton();
        assertInstanceOf(MacButton.class, button);
        assertEquals("mac", button.getStyle());
    }

    @Test
    void macFactoryCreatesMacTextBox() {
        AbstractFactory factory = new MacFactory();
        TextBox textBox = factory.createTextBox();
        assertInstanceOf(MacTextBox.class, textBox);
        assertEquals("mac", textBox.getStyle());
    }

    @Test
    void buttonAndTextBoxAreFromSameFamily() {
        AbstractFactory factory = new MacFactory();
        Button button = factory.createButton();
        TextBox textBox = factory.createTextBox();
        assertEquals(button.getStyle(), textBox.getStyle());
    }
}
