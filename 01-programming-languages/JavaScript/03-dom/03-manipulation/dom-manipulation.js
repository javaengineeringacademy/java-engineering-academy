/*
 * DOM Manipulation
 * createElement, appendChild, innerHTML
 */

// ============================================
// Running in Browser Context
// ============================================

// These examples work in browser console or when script is loaded in HTML

// ============================================
// Creating Elements
// ============================================

const div = document.createElement("div");
div.textContent = "Hello, World!";
div.classList.add("greeting");
div.id = "main-greeting";

document.body.appendChild(div);

// ============================================
// Adding Elements to DOM
// ============================================

const container = document.querySelector(".container");

// appendChild - adds to end
const newParagraph = document.createElement("p");
newParagraph.textContent = "New paragraph";
container.appendChild(newParagraph);

// append - adds to end (can add multiple)
container.append("Text node", newParagraph);

// prepend - adds to beginning
const firstItem = document.createElement("p");
firstItem.textContent = "First item";
container.prepend(firstItem);

// insertBefore
const reference = container.children[2];
const before = document.createElement("p");
before.textContent = "Inserted before";
container.insertBefore(before, reference);

// ============================================
// Removing Elements
// ============================================

const unwanted = document.querySelector(".unwanted");
if (unwanted) {
    unwanted.remove(); // Modern way
    // Or:
    // unwanted.parentNode.removeChild(unwanted);
}

// ============================================
// Replacing Elements
// ============================================

const oldElement = document.querySelector(".old");
const newElement = document.createElement("div");
newElement.textContent = "Replacement";

if (oldElement) {
    oldElement.parentNode.replaceChild(newElement, oldElement);
}

// ============================================
// innerHTML vs textContent vs innerText
// ============================================

const box = document.querySelector(".box");

// textContent - plain text (faster, safer)
box.textContent = "Hello <strong>World</strong>"; // Shows HTML as text

// innerHTML - parses HTML (potential XSS)
box.innerHTML = "Hello <strong>World</strong>"; // Renders HTML

// innerText - respects CSS styling
const hidden = document.querySelector(".hidden");
console.log(hidden.innerText); // Empty if hidden via CSS

// ============================================
// Attributes
// ============================================

const img = document.querySelector("img");

// Get attribute
const src = img.getAttribute("src");

// Set attribute
img.setAttribute("alt", "Description");
img.setAttribute("data-id", "123");

// Remove attribute
img.removeAttribute("title");

// Check if attribute exists
const hasAlt = img.hasAttribute("alt");

// ============================================
// Classes
// ============================================

const element = document.querySelector(".element");

// Add class
element.classList.add("active");

// Remove class
element.classList.remove("inactive");

// Toggle class
element.classList.toggle("selected");

// Check if class exists
const isActive = element.classList.contains("active");

// Replace class
element.classList.replace("old-class", "new-class");

// ============================================
// Styles (Inline)
// ============================================

const box2 = document.querySelector(".box");

// Set single style
box2.style.backgroundColor = "blue";
box2.style.fontSize = "16px";

// Set multiple styles using cssText
box2.style.cssText = "background-color: blue; font-size: 16px;";

// Get computed style
const computed = window.getComputedStyle(box2);
console.log("Actual color:", computed.backgroundColor);

// ============================================
// Dataset (data-* attributes)
// ============================================

const card = document.querySelector("[data-card-id]");

// Access data attributes
console.log("Card ID:", card.dataset.cardId);
console.log("Card Type:", card.dataset.cardType);

// Set data attributes
card.dataset.status = "active";
card.dataset.priority = "high";

// Remove data attribute
delete card.dataset.priority;

// ============================================
// Performance: DocumentFragment
// ============================================

// Create in memory, then add to DOM at once
const fragment = document.createDocumentFragment();

for (let i = 0; i < 100; i++) {
    const li = document.createElement("li");
    li.textContent = `Item ${i}`;
    fragment.appendChild(li);
}

// Single DOM update
const list = document.querySelector("ul");
list.appendChild(fragment);

// ============================================
// Clone Elements
// ============================================

const original = document.querySelector(".clone-me");
const clone = original.cloneNode(true); // Deep clone
document.body.appendChild(clone);
