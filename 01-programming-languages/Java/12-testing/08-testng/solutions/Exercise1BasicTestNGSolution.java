package academy.javaengineering.testing.testng.solutions;

import org.testng.annotations.*;

import static org.testng.Assert.*;

public class Exercise1BasicTestNGSolution {

    private int[] numbers;

    @BeforeClass
    public void setUp() {
        numbers = new int[]{1, 2, 3, 4, 5};
    }

    @Test(groups = {"math"})
    public void shouldCalculateSum() {
        int sum = 0;
        for (int n : numbers) sum += n;
        assertEquals(sum, 15);
    }

    @Test(groups = {"math"})
    public void shouldCalculateAverage() {
        int sum = 0;
        for (int n : numbers) sum += n;
        double avg = (double) sum / numbers.length;
        assertEquals(avg, 3.0, 0.001);
    }

    @Test(groups = {"string"})
    public void shouldConcatenateStrings() {
        String result = String.join("-", "a", "b", "c");
        assertEquals(result, "a-b-c");
    }

    @AfterClass
    public void tearDown() {
        numbers = null;
    }
}
