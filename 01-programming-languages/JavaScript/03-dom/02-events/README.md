# DOM Events

Handling user interactions and events in the DOM.

## Topics Covered

- addEventListener and removeEventListener
- Event object properties
- Event delegation
- preventDefault and stopPropagation

## Adding Event Listeners

```javascript
element.addEventListener("click", handler);
element.addEventListener("click", handler, { once: true });
element.removeEventListener("click", handler);
```

## Event Object

```javascript
element.addEventListener("click", (event) => {
    event.type;           // "click"
    event.target;         // Element that triggered event
    event.currentTarget;  // Element with listener
    event.preventDefault();  // Prevent default action
    event.stopPropagation(); // Stop bubbling
});
```

## Common Events

| Category | Events |
|----------|--------|
| Mouse | click, dblclick, mousedown, mouseup, mouseenter, mouseleave |
| Keyboard | keydown, keyup, keypress |
| Form | submit, change, input, focus, blur |
| Window | load, DOMContentLoaded, resize, scroll |

## Event Delegation

```javascript
const list = document.querySelector(".list");
list.addEventListener("click", (event) => {
    if (event.target.matches(".item")) {
        console.log("Item clicked:", event.target.textContent);
    }
});
```

## Running the Example

```bash
# Run in browser console or load HTML with script
node 03-dom/02-events/events.js
```
