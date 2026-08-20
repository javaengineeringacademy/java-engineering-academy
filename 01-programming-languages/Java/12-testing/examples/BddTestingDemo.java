package academy.javaengineering.testing.examples;

import java.util.*;

/**
 * BDD Testing Demo - Cucumber
 */
class BddTestingDemo {

    // ============================================
    // BDD Concept
    // ============================================

    /*
     * BDD (Behavior-Driven Development) extends TDD by writing
     * test cases in natural language that describe the behavior
     * of the system.
     * 
     * Structure:
     * - Feature: Describes what is being tested
     * - Scenario: A specific test case
     * - Given: Pre-conditions
     * - When: Action
     * - Then: Expected outcome
     * - And/But: Additional steps
     */

    // ============================================
    // Feature File (login.feature)
    // ============================================

    /*
     * Feature: User Login
     * 
     *   As a user
     *   I want to log in to my account
     *   So that I can access my personal information
     * 
     *   Scenario: Successful login with valid credentials
     *     Given the user is on the login page
     *     When the user enters valid username "admin" and password "admin123"
     *     Then the user should be redirected to the dashboard
     *     And a welcome message should be displayed
     * 
     *   Scenario: Failed login with invalid password
     *     Given the user is on the login page
     *     When the user enters valid username "admin" and invalid password "wrong"
     *     Then an error message "Invalid credentials" should be displayed
     *     And the user should remain on the login page
     * 
     *   Scenario: Login with empty fields
     *     Given the user is on the login page
     *     When the user leaves username and password empty
     *     And clicks the login button
     *     Then a validation message "Username and password are required" should be displayed
     * 
     *   Scenario Outline: Login with various credentials
     *     Given the user is on the login page
     *     When the user enters username "<username>" and password "<password>"
     *     Then the login result should be "<result>"
     * 
     *     Examples:
     *       | username | password | result    |
     *       | admin    | admin123 | success   |
     *       | admin    | wrong    | failure   |
     *       | invalid  | admin123 | failure   |
     *       |          | admin123 | validation|
     *       | admin    |          | validation|
     */

    // ============================================
    // Step Definitions (conceptual)
    // ============================================

    /*
     * @Given("the user is on the login page")
     * public void theUserIsOnTheLoginPage() {
     *     // Navigate to login page
     *     loginPage.navigateTo();
     * }
     * 
     * @When("the user enters valid username {string} and password {string}")
     * public void theUserEntersValidUsernameAndPassword(String username, String password) {
     *     loginPage.enterUsername(username);
     *     loginPage.enterPassword(password);
     *     loginPage.clickLogin();
     * }
     * 
     * @Then("the user should be redirected to the dashboard")
     * public void theUserShouldBeRedirectedToTheDashboard() {
     *     assertThat(dashboardPage.isDisplayed()).isTrue();
     * }
     * 
     * @Then("a welcome message should be displayed")
     * public void aWelcomeMessageShouldBeDisplayed() {
     *     assertThat(dashboardPage.getWelcomeMessage()).contains("Welcome");
     * }
     * 
     * @Then("an error message {string} should be displayed")
     * public void anErrorMessageShouldBeDisplayed(String message) {
     *     assertThat(loginPage.getErrorMessage()).isEqualTo(message);
     * }
     */

    // ============================================
    // Login Service Implementation
    // ============================================

    static class LoginService {
        private final Map<String, String> users = new HashMap<>();
        private String currentUser;
        private String lastError;

        LoginService() {
            users.put("admin", "admin123");
            users.put("user", "user123");
        }

        boolean login(String username, String password) {
            if (username == null || username.isEmpty()) {
                lastError = "Username and password are required";
                return false;
            }
            if (password == null || password.isEmpty()) {
                lastError = "Username and password are required";
                return false;
            }
            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username;
                lastError = null;
                return true;
            }
            lastError = "Invalid credentials";
            return false;
        }

        void logout() {
            currentUser = null;
        }

        String getCurrentUser() {
            return currentUser;
        }

        String getLastError() {
            return lastError;
        }

        boolean isLoggedIn() {
            return currentUser != null;
        }
    }

    // ============================================
    // Step Definition Implementation
    // ============================================

    static class LoginStepDefinitions {
        private LoginService loginService;
        private String loginPageMessage;

        LoginStepDefinitions() {
            loginService = new LoginService();
        }

        // Given
        void theUserIsOnTheLoginPage() {
            loginService.logout();
            loginPageMessage = "Login page loaded";
        }

        // When
        void theUserEntersValidUsernameAndPassword(String username, String password) {
            loginService.login(username, password);
        }

        void theUserLeavesUsernameAndPasswordEmpty() {
            loginService.login("", "");
        }

        void theUserEntersUsernameAndPassword(String username, String password) {
            loginService.login(username, password);
        }

        // Then
        boolean theUserShouldBeRedirectedToTheDashboard() {
            return loginService.isLoggedIn();
        }

        String aWelcomeMessageShouldBeDisplayed() {
            return "Welcome, " + loginService.getCurrentUser();
        }

        String anErrorMessageShouldBeDisplayed() {
            return loginService.getLastError();
        }

        boolean theUserShouldRemainOnTheLoginPage() {
            return !loginService.isLoggedIn();
        }
    }

    // ============================================
    // BDD Test Scenarios
    // ============================================

    static class LoginFeatureTest {
        static void runScenarios() {
            LoginStepDefinitions steps = new LoginStepDefinitions();

            System.out.println("Feature: User Login\n");

            // Scenario 1: Successful login
            System.out.println("  Scenario: Successful login with valid credentials");
            steps.theUserIsOnTheLoginPage();
            steps.theUserEntersValidUsernameAndPassword("admin", "admin123");
            assert steps.theUserShouldBeRedirectedToTheDashboard();
            assert steps.aWelcomeMessageShouldBeDisplayed().contains("Welcome");
            System.out.println("    ✓ PASSED\n");

            // Scenario 2: Failed login
            System.out.println("  Scenario: Failed login with invalid password");
            steps.theUserIsOnTheLoginPage();
            steps.theUserEntersValidUsernameAndPassword("admin", "wrong");
            assert steps.anErrorMessageShouldBeDisplayed().equals("Invalid credentials");
            assert steps.theUserShouldRemainOnTheLoginPage();
            System.out.println("    ✓ PASSED\n");

            // Scenario 3: Empty fields
            System.out.println("  Scenario: Login with empty fields");
            steps.theUserIsOnTheLoginPage();
            steps.theUserLeavesUsernameAndPasswordEmpty();
            assert steps.anErrorMessageShouldBeDisplayed().equals("Username and password are required");
            System.out.println("    ✓ PASSED\n");

            // Scenario Outline
            System.out.println("  Scenario Outline: Login with various credentials");
            String[][] examples = {
                {"admin", "admin123", "success"},
                {"admin", "wrong", "failure"},
                {"invalid", "admin123", "failure"},
                {"", "admin123", "validation"},
                {"admin", "", "validation"}
            };

            for (String[] example : examples) {
                steps.theUserIsOnTheLoginPage();
                steps.theUserEntersUsernameAndPassword(example[0], example[1]);
                boolean result = steps.theUserShouldBeRedirectedToTheDashboard();
                String expectedResult = example[2];
                boolean passed = (expectedResult.equals("success")) == result;
                System.out.printf("    %s with %s/%s: %s%n",
                    passed ? "✓" : "✗", example[0], example[1], passed ? "PASSED" : "FAILED");
            }
            System.out.println();
        }
    }

    // ============================================
    // Cucumber Configuration
    // ============================================

    /*
     * Maven Dependencies:
     * 
     * <dependency>
     *     <groupId>io.cucumber</groupId>
     *     <artifactId>cucumber-java</artifactId>
     *     <version>7.14.0</version>
     *     <scope>test</scope>
     * </dependency>
     * <dependency>
     *     <groupId>io.cucumber</groupId>
     *     <artifactId>cucumber-junit-platform-engine</artifactId>
     *     <version>7.14.0</version>
     *     <scope>test</scope>
     * </dependency>
     * 
     * Feature files location: src/test/resources/features/
     * Step definitions location: src/test/java/.../steps/
     */

    public static void main(String[] args) {
        System.out.println("=== BDD Testing Demo ===\n");

        LoginFeatureTest.runScenarios();

        System.out.println("--- BDD Best Practices ---");
        System.out.println("1. Write feature files collaboratively with stakeholders");
        System.out.println("2. Use domain language, not technical terms");
        System.out.println("3. Keep scenarios short and focused");
        System.out.println("4. Use Scenario Outlines for data-driven tests");
        System.out.println("5. Avoid testing implementation details");

        System.out.println("\n--- BDD Workflow ---");
        System.out.println("1. Discovery: Discuss behavior with team");
        System.out.println("2. Formulation: Write feature files");
        System.out.println("3. Automation: Implement step definitions");
        System.out.println("4. Validation: Run and verify tests");

        System.out.println("\n=== BDD Testing Demo Complete ===");
    }
}
