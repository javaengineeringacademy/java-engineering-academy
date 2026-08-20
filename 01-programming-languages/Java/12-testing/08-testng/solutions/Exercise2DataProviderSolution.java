package academy.javaengineering.testing.testng.solutions;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Exercise2DataProviderSolution {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"admin", "password123", true},
            {"user", "wrong", false},
            {"", "password123", false},
            {"admin", "", false}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expected) {
        boolean result = "admin".equals(username) && "password123".equals(password);
        assertEquals(result, expected);
    }

    @DataProvider(name = "stringOps")
    public Object[][] stringOperations() {
        return new Object[][] {
            {"hello", "upper", "HELLO"},
            {"HELLO", "lower", "hello"},
            {"hello", "reverse", "olleh"}
        };
    }

    @Test(dataProvider = "stringOps")
    public void testStringOperation(String input, String operation, String expected) {
        String result = switch (operation) {
            case "upper" -> input.toUpperCase();
            case "lower" -> input.toLowerCase();
            case "reverse" -> new StringBuilder(input).reverse().toString();
            default -> input;
        };
        assertEquals(result, expected);
    }
}
