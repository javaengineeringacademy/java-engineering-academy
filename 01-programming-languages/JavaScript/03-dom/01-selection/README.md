# DOM Selection

Selecting and finding elements in the DOM using JavaScript.

## Topics Covered

- getElementById
- querySelector and querySelectorAll
- CSS selector syntax
- Practical selection patterns

## Selection Methods

| Method | Returns | Use Case |
|--------|---------|----------|
| `getElementById()` | Single element | By ID attribute |
| `querySelector()` | First match | Complex selectors |
| `querySelectorAll()` | NodeList | Multiple elements |

## querySelector Examples

```javascript
// By tag
document.querySelector("p");

// By class
document.querySelector(".card");

// By ID
document.querySelector("#main");

// By attribute
document.querySelector('input[type="email"]');

// Complex selectors
document.querySelector("nav ul li:first-child a");
```

## Handling Null Results

```javascript
// Always check for null
const element = document.querySelector(".maybe-missing");
if (element) {
    element.classList.add("found");
}

// Optional chaining
const value = document.querySelector(".input")?.value;
```

## Running the Example

```bash
# Run in browser console or load HTML with script
node 03-dom/01-selection/selectors.js
```
