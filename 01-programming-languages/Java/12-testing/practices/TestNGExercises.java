package academy.javaengineering.testing.practices;

/**
 * TestNG Exercises
 * Practice TestNG framework features
 */
class TestNGExercises {

    // ============================================
    // Exercise 1: Groups
    // ============================================

    static class LoginService {
        boolean login(String username, String password) {
            if ("admin".equals(username) && "admin123".equals(password)) return true;
            if ("user".equals(username) && "user123".equals(password)) return true;
            return false;
        }

        boolean logout(String sessionId) {
            return sessionId != null && !sessionId.isEmpty();
        }
    }

    /*
     * TODO: Write tests with groups
     * @Test(groups = {"login", "smoke"})
     * void testValidLogin() { ... }
     * 
     * @Test(groups = {"login", "regression"})
     * void testInvalidLogin() { ... }
     * 
     * @Test(groups = {"logout"})
     * void testLogout() { ... }
     */

    // ============================================
    // Exercise 2: Data Provider
    // ============================================

    static class MathUtils {
        static int add(int a, int b) { return a + b; }
        static int multiply(int a, int b) { return a * b; }
    }

    /*
     * TODO: Implement data provider tests
     * @DataProvider(name = "additionData")
     * Object[][] additionData() {
     *     return new Object[][] {
     *         {1, 2, 3},
     *         {10, 20, 30},
     *         {-1, 1, 0}
     *     };
     * }
     * 
     * @Test(dataProvider = "additionData")
     * void testAddition(int a, int b, int expected) {
     *     assertEquals(MathUtils.add(a, b), expected);
     * }
     */

    // ============================================
    // Exercise 3: Dependencies
    // ============================================

    /*
     * TODO: Test method dependencies
     * @Test
     * void login() { ... }
     * 
     * @Test(dependsOnMethods = {"login"})
     * void dashboard() { ... }
     * 
     * @Test(dependsOnMethods = {"dashboard"})
     * void logout() { ... }
     */

    // ============================================
    // Exercise 4: Soft Assertions
    // ============================================

    static class Person {
        String name;
        int age;
        String email;

        Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }

    /*
     * TODO: Test multiple properties with soft assertions
     * SoftAssertions sa = new SoftAssertions();
     * sa.assertEquals(person.getName(), "John");
     * sa.assertEquals(person.getAge(), 30);
     * sa.assertAll();
     */

    // ============================================
    // Exercise 5: Parallel Execution
    // ============================================

    /*
     * TODO: Configure parallel test execution
     * - Test multiple independent scenarios
     * - Ensure thread safety
     * - Measure performance improvement
     */

    public static void main(String[] args) {
        System.out.println("=== TestNG Exercises ===");
        System.out.println("Practice TestNG framework features.");
    }
}
