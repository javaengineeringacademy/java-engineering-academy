package academy.javaengineering.testing.testng.practices;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Exercise 1: Basic TestNG
 *
 * Tasks:
 * 1. Create TestNG test class
 * 2. Use @BeforeClass and @AfterClass
 * 3. Write test methods with assertions
 * 4. Use groups for categorization
 */
public class Exercise1BasicTestNG {

    private int[] numbers;

    @BeforeClass
    public void setUp() {
        numbers = new int[]{1, 2, 3, 4, 5};
    }

    @Test(groups = {"math"})
    public void shouldCalculateSum() {
        // Arrange, Act, Assert
    }

    @Test(groups = {"math"})
    public void shouldCalculateAverage() {
        // Arrange, Act, Assert
    }

    @Test(groups = {"string"})
    public void shouldConcatenateStrings() {
        // Arrange, Act, Assert
    }
}
