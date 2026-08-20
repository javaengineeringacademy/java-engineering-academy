package academy.javaengineering.testing.testng.practices;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Exercise 2: DataProvider
 *
 * Tasks:
 * 1. Create a DataProvider for login validation
 * 2. Create a DataProvider for string operations
 * 3. Test multiple scenarios with data-driven approach
 */
public class Exercise2DataProvider {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        // TODO: Return test data for login validation
        return new Object[][] {};
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expected) {
        // TODO: Test login validation
    }

    @DataProvider(name = "stringOps")
    public Object[][] stringOperations() {
        // TODO: Return string operation test data
        return new Object[][] {};
    }

    @Test(dataProvider = "stringOps")
    public void testStringOperation(String input, String operation, String expected) {
        // TODO: Test string operations
    }
}
