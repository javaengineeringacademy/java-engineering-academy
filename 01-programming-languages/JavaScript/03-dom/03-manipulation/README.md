# DOM Manipulation

Creating, modifying, and removing DOM elements with JavaScript.

## Topics Covered

- Creating and inserting elements
- Removing and replacing elements
- textContent vs innerHTML
- Working with attributes and classes
- DocumentFragment for performance

## Creating Elements

```javascript
const div = document.createElement("div");
div.textContent = "Hello";
div.classList.add("greeting");
document.body.appendChild(div);
```

## Inserting Elements

| Method | Description |
|--------|-------------|
| `appendChild()` | Add to end of parent |
| `prepend()` | Add to beginning of parent |
| `append()` | Add to end (multiple args) |
| `insertBefore()` | Insert before reference node |
| `after()` | Insert after element |
| `before()` | Insert before element |

## textContent vs innerHTML

```javascript
// textContent - plain text, faster, safer
element.textContent = "Hello <strong>World</strong>";

// innerHTML - parses HTML, potential XSS
element.innerHTML = "Hello <strong>World</strong>";
```

**Always prefer `textContent`** unless you need to insert HTML.

## Working with Classes

```javascript
element.classList.add("active");
element.classList.remove("inactive");
element.classList.toggle("selected");
element.classList.contains("active");
element.classList.replace("old", "new");
```

## DocumentFragment

```javascript
const fragment = document.createDocumentFragment();
for (let i = 0; i < 100; i++) {
    const li = document.createElement("li");
    li.textContent = `Item ${i}`;
    fragment.appendChild(li);
}
list.appendChild(fragment); // Single DOM update
```

## Running the Example

```bash
# Run in browser console or load HTML with script
node 03-dom/03-manipulation/dom-manipulation.js
```
