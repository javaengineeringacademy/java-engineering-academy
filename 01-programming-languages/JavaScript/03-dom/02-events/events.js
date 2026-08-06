/*
 * DOM Events
 * addEventListener, event object, event delegation
 */

// ============================================
// Running in Browser Context
// ============================================

// These examples work in browser console or when script is loaded in HTML

// ============================================
// addEventListener
// ============================================

const button = document.querySelector(".btn");

// Basic click handler
button.addEventListener("click", () => {
    console.log("Button clicked!");
});

// Multiple handlers on same element
button.addEventListener("click", () => {
    console.log("Second handler");
});

// ============================================
// Event Object
// ============================================

button.addEventListener("click", (event) => {
    console.log("Event type:", event.type);        // "click"
    console.log("Target:", event.target);           // The element clicked
    console.log("Current target:", event.currentTarget); // Element with listener
    console.log("Client coordinates:", event.clientX, event.clientY);
    console.log("Page coordinates:", event.pageX, event.pageY);
});

// ============================================
// removeEventListener
// ============================================

function handleClick() {
    console.log("Clicked!");
}

button.addEventListener("click", handleClick);
button.removeEventListener("click", handleClick);

// Must pass same function reference
// This won't work:
// button.removeEventListener("click", () => handleClick());

// ============================================
// Common Event Types
// ============================================

// Mouse events
const element = document.querySelector(".interactive");

element.addEventListener("click", () => console.log("click"));
element.addEventListener("dblclick", () => console.log("double click"));
element.addEventListener("mousedown", () => console.log("mouse down"));
element.addEventListener("mouseup", () => console.log("mouse up"));
element.addEventListener("mouseenter", () => console.log("mouse enter"));
element.addEventListener("mouseleave", () => console.log("mouse leave"));

// Keyboard events
document.addEventListener("keydown", (e) => {
    console.log("Key:", e.key, "Code:", e.code);
});

document.addEventListener("keyup", (e) => {
    console.log("Key released:", e.key);
});

// Form events
const input = document.querySelector("input");
input.addEventListener("focus", () => console.log("focused"));
input.addEventListener("blur", () => console.log("blurred"));
input.addEventListener("input", (e) => console.log("value:", e.target.value));

// ============================================
// Event Delegation
// ============================================

// Instead of adding listeners to each child, add one to parent
const list = document.querySelector(".item-list");

list.addEventListener("click", (event) => {
    // Check if clicked element is an item
    if (event.target.matches(".item")) {
        console.log("Item clicked:", event.target.textContent);
    }
});

// Alternative: check closest
list.addEventListener("click", (event) => {
    const item = event.target.closest(".item");
    if (item && list.contains(item)) {
        console.log("Item clicked:", item.textContent);
    }
});

// ============================================
// preventDefault and stopPropagation
// ============================================

const form = document.querySelector("form");

form.addEventListener("submit", (event) => {
    event.preventDefault(); // Stop form from submitting
    console.log("Form submitted (prevented)");
});

const link = document.querySelector("a.external");
link.addEventListener("click", (event) => {
    event.preventDefault(); // Stop navigation
    console.log("Link clicked (prevented)");
});

// stopPropagation - prevent event from bubbling up
const outer = document.querySelector(".outer");
const inner = document.querySelector(".inner");

inner.addEventListener("click", (event) => {
    event.stopPropagation(); // Don't bubble to outer
    console.log("Inner clicked");
});

outer.addEventListener("click", () => {
    console.log("Outer clicked"); // Won't fire if inner stops propagation
});

// ============================================
// Event Listener Options
// ============================================

button.addEventListener("click", handler, {
    capture: false,   // Use capture phase (default: false)
    once: true,       // Remove after first invocation
    passive: false,   // Never calls preventDefault
    signal: controller.signal  // AbortController signal
});

// ============================================
// once option
// ============================================

button.addEventListener("click", () => {
    console.log("This will only fire once");
}, { once: true });
