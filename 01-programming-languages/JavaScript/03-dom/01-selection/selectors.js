/*
 * DOM Selection
 * getElementById, querySelector, querySelectorAll
 */

// ============================================
// Running in Browser Context
// ============================================

// These examples work in browser console or when script is loaded in HTML

// ============================================
// getElementById
// ============================================

// Selects element by its ID attribute
const header = document.getElementById("main-header");
console.log(header); // <h1 id="main-header">...</h1>

// Returns null if not found
const missing = document.getElementById("nonexistent");
console.log(missing); // null

// ============================================
// querySelector
// ============================================

// Returns FIRST matching element using CSS selector

// By tag name
const firstParagraph = document.querySelector("p");
console.log(firstParagraph);

// By class
const primaryButton = document.querySelector(".btn-primary");
console.log(primaryButton);

// By ID
const nav = document.querySelector("#navigation");
console.log(nav);

// Attribute selector
const link = document.querySelector('a[href="https://example.com"]');
console.log(link);

// Pseudo-class selector
const firstItem = document.querySelector("li:first-child");
console.log(firstItem);

// Complex selectors
const nested = document.querySelector("div.container > p.intro");
console.log(nested);

// Returns null if not found
const notFound = document.querySelector(".nonexistent");
console.log(notFound); // null

// ============================================
// querySelectorAll
// ============================================

// Returns NodeList of ALL matching elements

// All paragraphs
const allParagraphs = document.querySelectorAll("p");
console.log("Paragraphs:", allParagraphs.length);

// All buttons with specific class
const buttons = document.querySelectorAll("button.btn");
buttons.forEach(btn => {
    console.log(btn.textContent);
});

// Convert NodeList to Array for array methods
const buttonArray = Array.from(buttons);
buttonArray.filter(btn => btn.classList.contains("active"));

// Spread operator also works
const [...allLinks] = document.querySelectorAll("a");

// ============================================
// Practical Selection Patterns
// ============================================

// Select multiple related elements
const form = document.querySelector("form");
const inputs = form.querySelectorAll("input");
const submitBtn = form.querySelector('button[type="submit"]');

// Select with context
const container = document.querySelector(".container");
const items = container.querySelectorAll(".item");

// Select within selected element
const sidebar = document.querySelector(".sidebar");
const sidebarLinks = sidebar ? sidebar.querySelectorAll("a") : [];

// ============================================
// Handling Selection Results
// ============================================

// Always check for null
const element = document.querySelector(".might-not-exist");
if (element) {
    element.classList.add("found");
}

// Optional chaining
const value = document.querySelector(".input")?.value;

// ============================================
// Performance Tips
// ============================================

// 1. Use specific selectors (ID > class > tag)
// 2. Cache selections if used multiple times
// 3. Avoid querySelectorAll in loops
// 4. Use documentFragment for batch DOM operations
