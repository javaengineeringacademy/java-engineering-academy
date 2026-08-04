# Playwright - End-to-End Testing

## Overview

Playwright is a modern E2E testing framework for web applications. It provides reliable cross-browser testing with automatic waits, code generation, and powerful selectors.

## Setup

### Maven

```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.40.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'com.microsoft.playwright:playwright:1.40.0'
}
```

### Install Browsers

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Basic Usage

### Page Navigation

```java
class PlaywrightTest {

    static Playwright playwright;
    static Browser browser;
    static Page page;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    void shouldNavigateToPage() {
        page.navigate("https://example.com");
        assertEquals("Example Domain", page.title());
    }

    @AfterAll
    static void teardown() {
        browser.close();
        playwright.close();
    }
}
```

### Click and Fill

```java
@Test
void shouldFillForm() {
    page.navigate("https://example.com/form");
    page.locator("#name").fill("John Doe");
    page.locator("#email").fill("john@example.com");
    page.locator("#submit").click();

    assertEquals("Thank you!", page.locator(".success-message").textContent());
}
```

## Selectors

### CSS Selectors

```java
// By ID
page.locator("#login-button").click();

// By class
page.locator(".form-input").fill("text");

// By attribute
page.locator("[data-testid='submit']").click();

// Complex selectors
page.locator("form > div > button[type='submit']").click();
```

### Text Selectors

```java
// Exact text
page.locator("text=Submit").click();

// Partial text
page.locator("text=Submit Form").click();

// Case insensitive
page.locator("text=submit").click();
```

### Role Selectors

```java
// By role
page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Submit")).click();
page.getByRole(AriaRole.TEXTBOX, new GetByRoleOptions().setName("Email")).fill("test@test.com");
page.getByRole(AriaRole.LINK, new GetByRoleOptions().setName("Home")).click();
```

### Other Selectors

```java
// By label
page.getByLabel("Email").fill("test@test.com");

// By placeholder
page.getByPlaceholder("Enter your email").fill("test@test.com");

// By text content
page.getByText("Welcome").click();

// By test ID
page.getByTestId("login-form").fill("test@test.com");
```

## Assertions

### Page Assertions

```java
@Test
void shouldHaveTitle() {
    page.navigate("https://example.com");
    assertEquals("Example Domain", page.title());
    assertEquals("https://example.com/", page.url());
}
```

### Locator Assertions

```java
@Test
void shouldDisplayMessage() {
    Locator message = page.locator(".success-message");
    assertEquals(true, message.isVisible());
    assertEquals("Success!", message.textContent());
}
```

### expect API

```java
@Test
void shouldHaveText() {
    assertThat(page.locator(".title")).hasText("Welcome");
    assertThat(page.locator(".count")).hasText("5");
}
```

## Waiting

### Auto-waiting

```java
// Playwright auto-waits for elements
page.locator("#submit").click(); // Waits for element to be visible, enabled
page.locator("#input").fill("text"); // Waits for element to be editable
```

### Explicit Waits

```java
// Wait for selector
page.waitForSelector(".loaded");

// Wait for URL
page.waitForURL("**/success");

// Wait for function
page.waitForFunction("() => document.querySelector('.loaded') !== null");

// Wait for timeout
page.waitForTimeout(5000); // 5 seconds
```

## Screenshots and Videos

### Screenshots

```java
@Test
void shouldTakeScreenshot() {
    page.screenshot(new Page.ScreenshotOptions()
        .setPath(Path.of("screenshot.png"))
        .setFullPage(true));
}
```

### Videos

```java
BrowserContext context = browser.newContext(new Browser.NewContextOptions()
    .setRecordVideoDir(Path.of("videos/"))
    .setRecordVideoSize(1280, 720));
```

## Best Practices

```java
// Use auto-waiting
page.locator("#submit").click(); // Good

// Avoid manual waits
page.waitForTimeout(1000); // Bad
page.locator("#submit").click();

// Use test IDs for stability
page.getByTestId("submit").click();

// Use role selectors for accessibility
page.getByRole(AriaRole.BUTTON, new GetByRoleOptions().setName("Submit")).click();
```

## Resources

- [Playwright Java](https://playwright.dev/java/)
- [Playwright GitHub](https://github.com/microsoft/playwright-java)
- [Playwright API](https://playwright.dev/java/docs/api/class-page)
