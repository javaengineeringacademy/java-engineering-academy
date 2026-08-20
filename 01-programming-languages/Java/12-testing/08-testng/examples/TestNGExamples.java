package academy.javaengineering.testing.testng.examples;

import org.testng.annotations.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

public class TestNGExamples {

    private static int counter = 0;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Before Suite");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("Before Class");
    }

    @BeforeMethod
    public void beforeMethod() {
        counter = 0;
    }

    @Test
    public void testAddition() {
        assertEquals(2 + 3, 5, "Addition should work");
    }

    @Test
    public void testSubstring() {
        assertEquals("hello".substring(0, 3), "hel");
    }

    @DataProvider(name = "mathData")
    public Object[][] mathData() {
        return new Object[][] {
            {1, 1, 2},
            {2, 3, 5},
            {10, 20, 30}
        };
    }

    @Test(dataProvider = "mathData")
    public void testAdditionWithDataProvider(int a, int b, int expected) {
        assertEquals(a + b, expected);
    }

    @Test(groups = {"fast"})
    public void fastTest() {
        assertTrue(true);
    }

    @Test(groups = {"slow"})
    public void slowTest() throws InterruptedException {
        Thread.sleep(100);
        assertTrue(true);
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("After Method");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("After Class");
    }
}
