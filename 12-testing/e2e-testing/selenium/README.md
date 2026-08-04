# Selenium - Browser Automation

## Overview

Selenium is a widely-used framework for automating web browsers. It supports multiple languages and browsers, making it ideal for cross-browser E2E testing.

## Setup

### Maven

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.16.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
dependencies {
    testImplementation 'org.seleniumhq.selenium:selenium-java:4.16.0'
    testImplementation 'io.github.bonigarcia:webdrivermanager:5.6.2'
}
```

## Basic Usage

### WebDriver Setup

```java
class SeleniumTest {

    static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    void shouldNavigateToPage() {
        driver.get("https://example.com");
        assertEquals("Example Domain", driver.getTitle());
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }
}
```

### Finding Elements

```java
@Test
void shouldFindElements() {
    driver.get("https://example.com");

    // By ID
    WebElement heading = driver.findElement(By.id("heading"));

    // By class name
    WebElement paragraph = driver.findElement(By.className("content"));

    // By name
    WebElement input = driver.findElement(By.name("email"));

    // By tag name
    List<WebElement> links = driver.findElements(By.tagName("a"));

    // By CSS selector
    WebElement button = driver.findElement(By.cssSelector("button.submit"));

    // By XPath
    WebElement element = driver.findElement(By.xpath("//div[@class='content']//p"));
}
```

### Interacting with Elements

```java
@Test
void shouldInteractWithElements() {
    driver.get("https://example.com/form");

    // Click
    driver.findElement(By.id("submit")).click();

    // Type text
    WebElement input = driver.findElement(By.name("email"));
    input.clear();
    input.sendKeys("john@example.com");

    // Submit form
    input.submit();

    // Get attributes
    String value = input.getAttribute("value");
    String type = input.getAttribute("type");

    // Get text
    String text = driver.findElement(By.tagName("h1")).getText();
}
```

## Waits

### Implicit Wait

```java
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

### Explicit Wait

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

// Wait for element to be visible
WebElement element = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("loading"))
);

// Wait for element to be clickable
wait.until(
    ExpectedConditions.elementToBeClickable(By.id("submit"))
).click();

// Wait for text to be present
wait.until(
    ExpectedConditions.textToBePresentInElement(
        driver.findElement(By.id("status")),
        "Complete"
    )
);
```

### Fluent Wait

```java
FluentWait<WebDriver> wait = new FluentWait<>(driver)
    .withTimeout(Duration.ofSeconds(30))
    .pollingEvery(Duration.ofSeconds(1))
    .ignoring(NoSuchElementException.class);

WebElement element = wait.until(d -> d.findElement(By.id("dynamic")));
```

## Advanced Features

### Window Management

```java
// Switch to new window
String originalWindow = driver.getWindowHandle();
for (String handle : driver.getWindowHandles()) {
    if (!handle.equals(originalWindow)) {
        driver.switchTo().window(handle);
        break;
    }
}

// Switch to iframe
driver.switchTo().frame("iframe-name");
driver.switchTo().defaultContent();

// Switch to alert
Alert alert = driver.switchTo().alert();
alert.accept(); // or alert.dismiss();
```

### Actions

```java
Actions actions = new Actions(driver);

// Hover
actions.moveToElement(element).perform();

// Double click
actions.doubleClick(element).perform();

// Drag and drop
actions.dragAndDrop(source, target).perform();

// Key actions
actions.keyDown(Keys.CONTROL).click(element).keyUp(Keys.CONTROL).perform();
```

### JavaScript Execution

```java
JavascriptExecutor js = (JavascriptExecutor) driver;

// Execute script
js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

// Execute async script
js.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 5000)");

// Get element
WebElement element = (WebElement) js.executeScript("return document.getElementById('id')");
```

## Page Object Model

```java
public class LoginPage {

    private final WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}

// Usage
@Test
void shouldLogin() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login("admin", "password");
    assertEquals("Dashboard", driver.getTitle());
}
```

## Cross-Browser Testing

```java
@ParameterizedTest
@ValueSource(strings = {"chrome", "firefox", "edge"})
void shouldWorkOnMultipleBrowsers(String browser) {
    WebDriver driver;
    switch (browser) {
        case "firefox":
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            break;
        case "edge":
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            break;
        default:
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
    }

    driver.get("https://example.com");
    assertEquals("Example Domain", driver.getTitle());
    driver.quit();
}
```

## Best Practices

```java
// Use Page Object Model
LoginPage loginPage = new LoginPage(driver);
loginPage.login("admin", "password");

// Use explicit waits
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("result")));

// Use CSS selectors over XPath
driver.findElement(By.cssSelector("button.submit")); // Better
// driver.findElement(By.xpath("//button[@class='submit']")); // Slower

// Clean up after tests
@AfterEach
void cleanup() {
    driver.manage().deleteAllCookies();
}
```

## Resources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Selenium GitHub](https://github.com/SeleniumHQ/selenium)
- [WebDriverManager](https://bonigarcia.dev/webdrivermanager/)
