# BDD with Cucumber

## Overview
BDD extends TDD with natural language Gherkin syntax.

## Gherkin Syntax
```gherkin
Feature: User Registration
  As a new user
  I want to register for an account

  Scenario: Successful registration
    When the user enters valid email "john@example.com"
    And the user enters password "SecurePass123"
    And the user clicks register
    Then the user should see a success message

  Scenario Outline: Password validation
    When the user enters password "<password>"
    Then the password should be "<result>"
    Examples:
      | password       | result  |
      | SecurePass123  | valid   |
      | short          | invalid |
```

## Step Definitions
```java
public class RegistrationSteps {
    @Given("the application is running")
    public void applicationIsRunning() { driver.get("http://localhost:8080"); }

    @When("the user enters valid email {string}")
    public void userEntersEmail(String email) { registrationPage.enterEmail(email); }

    @When("the user enters password {string}")
    public void userEntersPassword(String password) { registrationPage.enterPassword(password); }

    @When("the user clicks register")
    public void userClicksRegister() { registrationPage.submit(); }

    @Then("the user should see a success message")
    public void userShouldSeeSuccess() {
        assertTrue(registrationPage.getMessage().contains("successful"));
    }
}
```

## Hooks
```java
public class Hooks {
    @Before public void setUp() { WebDriverManager.chromedriver().setup(); }
    @After public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) takeScreenshot(scenario.getName());
        driver.quit();
    }
}
```

## Best Practices
1. Write scenarios in business language
2. Keep scenarios short (3-5 steps)
3. Use Background for common setup
4. Step definitions should be reusable
