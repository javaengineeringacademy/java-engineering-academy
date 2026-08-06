# Jest Testing

Unit testing JavaScript with Jest framework.

## Topics Covered

- Setting up Jest
- Writing tests with `describe` and `it`
- Using `expect` matchers
- Testing async code
- Setup and teardown hooks

## Basic Test Structure

```javascript
describe("Function Name", () => {
    it("should do something", () => {
        expect(functionCall()).toBe(expectedValue);
    });
});
```

## Common Matchers

| Matcher | Description |
|---------|-------------|
| `toBe()` | Strict equality |
| `toEqual()` | Deep equality |
| `toBeTruthy()` | Truthy value |
| `toBeFalsy()` | Falsy value |
| `toContain()` | Contains item |
| `toHaveLength()` | Array/string length |
| `toThrow()` | Throws error |
| `toMatch()` | String matches regex |

## Async Testing

```javascript
// async/await
it("should fetch data", async () => {
    const data = await fetchData();
    expect(data).toBeDefined();
});

// Promise
it("should fetch data", () => {
    return fetchData().then(data => {
        expect(data).toBeDefined();
    });
});
```

## Running Jest

```bash
npx jest                    # Run all tests
npx jest filename.js        # Run specific file
npx jest --coverage         # With coverage
npx jest --watch            # Watch mode
npx jest -t "test name"    # Run matching test
```

## Running the Example

```bash
npx jest 05-testing/01-jest/test-example.js
```
