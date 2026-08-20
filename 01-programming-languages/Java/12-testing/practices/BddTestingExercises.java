package academy.javaengineering.testing.practices;

/**
 * BDD Testing Exercises
 * Practice BDD with Cucumber
 */
class BddTestingExercises {

    // ============================================
    // Exercise 1: Feature File
    // ============================================

    /*
     * TODO: Write a feature file for a shopping cart
     * 
     * Feature: Shopping Cart
     * 
     *   As a customer
     *   I want to manage my shopping cart
     *   So that I can purchase items
     * 
     *   Scenario: Add item to cart
     *     Given the cart is empty
     *     When I add "Laptop" to the cart
     *     Then the cart should contain 1 item
     * 
     *   Scenario: Remove item from cart
     *     Given the cart has "Laptop"
     *     When I remove "Laptop" from the cart
     *     Then the cart should be empty
     * 
     *   Scenario: Apply discount code
     *     Given the cart has items totaling $100
     *     When I apply discount code "SAVE10"
     *     Then the total should be $90
     */

    // ============================================
    // Exercise 2: Step Definitions
    // ============================================

    static class ShoppingCart {
        private final java.util.List<String> items = new java.util.ArrayList<>();
        private double total = 0;
        private String discountCode;

        void addItem(String item, double price) {
            items.add(item);
            total += price;
        }

        void removeItem(String item) {
            items.remove(item);
        }

        void applyDiscount(String code) {
            discountCode = code;
            if ("SAVE10".equals(code)) {
                total *= 0.9;
            } else if ("SAVE20".equals(code)) {
                total *= 0.8;
            }
        }

        int getItemCount() { return items.size(); }
        double getTotal() { return total; }
        boolean isEmpty() { return items.isEmpty(); }
    }

    /*
     * TODO: Implement step definitions
     * 
     * @Given("the cart is empty")
     * public void theCartIsEmpty() {
     *     cart = new ShoppingCart();
     * }
     * 
     * @When("I add {string} to the cart")
     * public void iAddItemToTheCart(String item) {
     *     cart.addItem(item, 99.99);
     * }
     * 
     * @Then("the cart should contain {int} item(s)")
     * public void theCartShouldContainItems(int count) {
     *     assertEquals(count, cart.getItemCount());
     * }
     */

    // ============================================
    // Exercise 3: Scenario Outline
    // ============================================

    /*
     * TODO: Write scenario outline for login
     * 
     * Scenario Outline: Login with various credentials
     *   Given the user is on the login page
     *   When the user enters username "<username>" and password "<password>"
     *   Then the login result should be "<result>"
     * 
     *   Examples:
     *     | username | password | result  |
     *     | admin    | admin123 | success |
     *     | admin    | wrong    | failure |
     *     | invalid  | admin123 | failure |
     */

    // ============================================
    // Exercise 4: Background
    // ============================================

    /*
     * TODO: Use background for common setup
     * 
     * Feature: User Management
     * 
     *   Background:
     *     Given the database has test users
     *     And the user is authenticated
     * 
     *   Scenario: View user profile
     *     When the user views their profile
     *     Then the profile should be displayed
     * 
     *   Scenario: Update user profile
     *     When the user updates their name to "John"
     *     Then the name should be "John"
     */

    // ============================================
    // Exercise 5: Tags and Hooks
    // ============================================

    /*
     * TODO: Use tags for test organization
     * 
     * @smoke
     * Scenario: Critical login test
     *   Given ...
     * 
     * @regression
     * Scenario: Full login flow
     *   Given ...
     * 
     * Hooks:
     * @Before
     * public void setUp() { ... }
     * 
     * @After
     * public void tearDown() { ... }
     * 
     * @Before("@smoke")
     * public void setUpSmokeTest() { ... }
     */

    public static void main(String[] args) {
        System.out.println("=== BDD Testing Exercises ===");
        System.out.println("Practice BDD with Cucumber.");
        System.out.println("Write feature files and step definitions.");
    }
}
