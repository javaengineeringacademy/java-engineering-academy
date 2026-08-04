# CSS3 Complete Guide

## Table of Contents

1. [Selectors](#selectors)
2. [Box Model](#box-model)
3. [Flexbox](#flexbox)
4. [CSS Grid](#css-grid)
5. [CSS Variables](#css-variables)
6. [Animations and Transitions](#animations-and-transitions)
7. [Responsive Design](#responsive-design)
8. [Typography](#typography)
9. [Positioning](#positioning)
10. [Modern CSS Features](#modern-css-features)

---

## Selectors

### Basic Selectors

```css
/* Universal selector */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* Type/element selector */
p {
    line-height: 1.6;
}

/* Class selector */
.card {
    border: 1px solid #ddd;
    border-radius: 8px;
}

/* ID selector */
#header {
    background-color: #333;
    color: white;
}

/* Attribute selector */
input[type="email"] {
    border: 2px solid blue;
}

a[href^="https"] {
    color: green;
}

a[href$=".pdf"] {
    color: red;
}

img[alt~="photo"] {
    border: 1px solid #ccc;
}

input[required] {
    border-left: 3px solid red;
}
```

### Combinators

```css
/* Descendant selector */
nav a {
    text-decoration: none;
}

/* Child selector */
nav > a {
    display: block;
    padding: 10px;
}

/* Adjacent sibling */
h2 + p {
    font-size: 1.2em;
    font-weight: bold;
}

/* General sibling */
h2 ~ p {
    margin-top: 1em;
}
```

### Pseudo-Classes

```css
/* Link states */
a:link { color: blue; }
a:visited { color: purple; }
a:hover { color: red; }
a:active { color: orange; }

/* Form states */
input:focus {
    outline: 2px solid #007bff;
    border-color: #007bff;
}

input:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

input:invalid {
    border-color: red;
}

input:valid {
    border-color: green;
}

input:placeholder-shown {
    border-style: dashed;
}

/* Structural pseudo-classes */
li:first-child { font-weight: bold; }
li:last-child { border-bottom: none; }
li:nth-child(odd) { background-color: #f5f5f5; }
li:nth-child(3n) { color: red; }
li:not(:last-child) { border-bottom: 1px solid #ddd; }

/* Position pseudo-classes */
p:empty {
    display: none;
}

/* Negation */
input:not([type="submit"]):not([type="reset"]) {
    border: 1px solid #999;
}
```

### Pseudo-Elements

```css
/* ::before and ::after */
.quote::before {
    content: '"';
    font-size: 2em;
    color: #999;
}

.quote::after {
    content: '"';
    font-size: 2em;
    color: #999;
}

/* First letter and line */
p:first-letter {
    font-size: 2em;
    font-weight: bold;
    float: left;
    margin-right: 5px;
}

p:first-line {
    font-variant: small-caps;
}

/* Selection */
::selection {
    background-color: #3498db;
    color: white;
}

/* Placeholder styling */
::placeholder {
    color: #aaa;
    font-style: italic;
}

/* Tooltip with data attribute */
[data-tooltip]::after {
    content: attr(data-tooltip);
    position: absolute;
    bottom: 100%;
    left: 50%;
    transform: translateX(-50%);
    background: #333;
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    white-space: nowrap;
    opacity: 0;
    pointer-events: none;
    transition: opacity 0.2s;
}

[data-tooltip]:hover::after {
    opacity: 1;
}
```

### Specificity

Specificity determines which CSS rule wins when multiple rules target the same element.

```
Inline styles:     1,0,0,0  (1000)
IDs:               0,1,0,0  (100)
Classes/attrs:     0,0,1,0  (10)
Elements/pseudo:   0,0,0,1  (1)
```

```css
/* Specificity: 0,0,1,1 — wins over type selectors */
button.primary {
    background: blue;
}

/* Specificity: 0,1,0,1 — wins over class */
#submit-btn {
    background: green;
}

/* !important overrides specificity (avoid when possible) */
.urgent {
    color: red !important;
}
```

---

## Box Model

```css
/* Default box model */
.element {
    width: 200px;
    padding: 20px;
    border: 2px solid #333;
    margin: 10px;
    /* Total width: 200 + 20*2 + 2*2 + 10*2 = 264px */
}

/* Border-box model (recommended) */
*,
*::before,
*::after {
    box-sizing: border-box;
}

.element {
    width: 200px;
    padding: 20px;
    border: 2px solid #333;
    margin: 10px;
    /* Total width: 200px */
}

/* Box sizing per element */
.card {
    box-sizing: border-box;
}

/* Shorthand properties */
.element {
    /* margin: top right bottom left */
    margin: 10px 20px 10px 20px;
    /* margin: vertical horizontal */
    margin: 10px 20px;
    /* margin: all sides */
    margin: 10px;

    /* Same for padding */
    padding: 10px 20px;

    /* Border */
    border: 1px solid #333;
    border-width: 1px 2px 1px 2px;
    border-style: solid dashed;
    border-color: red blue;
}

/* Outline (does not affect layout) */
input:focus {
    outline: 3px solid #007bff;
    outline-offset: 2px;
}

/* Overflow */
.container {
    overflow: hidden;      /* clips content */
    overflow: scroll;      /* adds scrollbars */
    overflow: auto;        /* scrollbar only if needed */
    overflow-x: scroll;    /* horizontal only */
    overflow-y: auto;      /* vertical only */
}
```

---

## Flexbox

Flexbox provides one-dimensional layout capabilities.

### Container Properties

```css
.flex-container {
    display: flex;

    /* Direction */
    flex-direction: row;             /* horizontal (default) */
    flex-direction: row-reverse;     /* horizontal, reversed */
    flex-direction: column;          /* vertical */
    flex-direction: column-reverse;  /* vertical, reversed */

    /* Wrapping */
    flex-wrap: nowrap;      /* single line (default) */
    flex-wrap: wrap;        /* multi-line */
    flex-wrap: wrap-reverse; /* multi-line, reversed */

    /* Main axis alignment */
    justify-content: flex-start;      /* start (default) */
    justify-content: flex-end;        /* end */
    justify-content: center;          /* center */
    justify-content: space-between;   /* equal space between */
    justify-content: space-around;    /* equal space around */
    justify-content: space-evenly;    /* equal space everywhere */

    /* Cross axis alignment */
    align-items: stretch;     /* fill container (default) */
    align-items: flex-start;  /* start of cross axis */
    align-items: flex-end;    /* end of cross axis */
    align-items: center;      /* center of cross axis */
    align-items: baseline;    /* align by text baseline */

    /* Multi-line cross axis alignment */
    align-content: flex-start;
    align-content: center;
    align-content: space-between;

    /* Gap */
    gap: 10px;
    row-gap: 10px;
    column-gap: 20px;
}
```

### Item Properties

```css
.flex-item {
    /* Growth factor */
    flex-grow: 0;    /* don't grow (default) */
    flex-grow: 1;    /* grow to fill space */

    /* Shrink factor */
    flex-shrink: 1;  /* can shrink (default) */
    flex-shrink: 0;  /* don't shrink */

    /* Basis */
    flex-basis: auto;        /* based on content */
    flex-basis: 200px;       /* fixed width */
    flex-basis: 50%;         /* percentage */

    /* Shorthand: flex: grow shrink basis */
    flex: 1;                 /* grow equally */
    flex: 0 0 200px;         /* fixed size, no grow/shrink */
    flex: 1 1 0;             /* grow equally from 0 */

    /* Individual alignment */
    align-self: flex-start;
    align-self: center;
    align-self: stretch;

    /* Order (default is 0) */
    order: -1;   /* before other items */
    order: 1;    /* after other items */
}
```

### Flexbox Patterns

```css
/* Centering */
.center-all {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
}

/* Sticky footer layout */
.page {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.page-content {
    flex: 1;
}

/* Card grid */
.card-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
}

.card {
    flex: 1 1 300px;  /* grow, shrink, min-width */
    max-width: 100%;
}

/* Navbar */
.navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
}

.nav-links {
    display: flex;
    gap: 20px;
    list-style: none;
}

/* Holy grail layout */
.layout {
    display: flex;
    min-height: 100vh;
}

.sidebar-left { flex: 0 0 250px; }
.content { flex: 1; }
.sidebar-right { flex: 0 0 200px; }
```

---

## CSS Grid

Grid provides two-dimensional layout capabilities.

### Container Properties

```css
.grid-container {
    display: grid;

    /* Explicit columns and rows */
    grid-template-columns: 200px 1fr 200px;
    grid-template-rows: 80px 1fr 60px;

    /* Repeat notation */
    grid-template-columns: repeat(3, 1fr);
    grid-template-columns: repeat(4, minmax(200px, 1fr));
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

    /* Named lines */
    grid-template-columns: [sidebar-start] 200px [sidebar-end main-start] 1fr [main-end];

    /* Gap */
    gap: 20px;
    row-gap: 20px;
    column-gap: 10px;

    /* Justify and align (for all items) */
    justify-items: start;
    align-items: center;

    /* Align grid tracks */
    justify-content: center;
    align-content: center;
}
```

### Item Properties

```css
.grid-item {
    /* Span multiple columns/rows */
    grid-column: 1 / 3;        /* from line 1 to line 3 */
    grid-column: span 2;       /* span 2 columns */
    grid-column: 1 / -1;       /* full width */

    grid-row: 1 / span 3;     /* span 3 rows from line 1 */

    /* Named areas */
    grid-area: header;

    /* Individual alignment */
    justify-self: center;
    align-self: end;
}
```

### Grid Template Areas

```css
.layout {
    display: grid;
    grid-template-columns: 250px 1fr;
    grid-template-rows: 80px 1fr 60px;
    grid-template-areas:
        "header  header"
        "sidebar content"
        "footer  footer";
    gap: 10px;
    min-height: 100vh;
}

.header  { grid-area: header; }
.sidebar { grid-area: sidebar; }
.content { grid-area: content; }
.footer  { grid-area: footer; }

/* Responsive without media queries */
@media (max-width: 768px) {
    .layout {
        grid-template-columns: 1fr;
        grid-template-areas:
            "header"
            "content"
            "sidebar"
            "footer";
    }
}
```

### Grid Patterns

```css
/* Responsive card grid */
.gallery {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 16px;
}

/* Dashboard layout */
.dashboard {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-auto-rows: minmax(150px, auto);
    gap: 16px;
}

.widget-wide {
    grid-column: span 2;
}

.widget-tall {
    grid-row: span 2;
}

/* Overlapping elements */
.hero {
    display: grid;
    place-items: center;
}

.hero-title {
    grid-area: 1 / 1;  /* same cell as hero */
}

.hero-image {
    grid-area: 1 / 1;
    opacity: 0.5;
}
```

---

## CSS Variables

```css
:root {
    /* Color palette */
    --color-primary: #3498db;
    --color-primary-dark: #2980b9;
    --color-secondary: #2ecc71;
    --color-danger: #e74c3c;
    --color-warning: #f39c12;
    --color-success: #27ae60;

    /* Neutrals */
    --color-text: #2c3e50;
    --color-text-light: #7f8c8d;
    --color-bg: #ffffff;
    --color-bg-alt: #f8f9fa;
    --color-border: #dee2e6;

    /* Spacing scale */
    --space-xs: 4px;
    --space-sm: 8px;
    --space-md: 16px;
    --space-lg: 24px;
    --space-xl: 32px;
    --space-2xl: 48px;

    /* Typography */
    --font-family: 'Inter', -apple-system, sans-serif;
    --font-mono: 'Fira Code', monospace;
    --font-size-xs: 0.75rem;
    --font-size-sm: 0.875rem;
    --font-size-base: 1rem;
    --font-size-lg: 1.125rem;
    --font-size-xl: 1.25rem;
    --font-weight-normal: 400;
    --font-weight-bold: 700;

    /* Shadows */
    --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
    --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
    --shadow-lg: 0 10px 15px rgba(0, 0, 0, 0.1);

    /* Border radius */
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;

    /* Transitions */
    --transition-fast: 150ms ease;
    --transition-normal: 300ms ease;
    --transition-slow: 500ms ease;

    /* Z-index scale */
    --z-dropdown: 100;
    --z-sticky: 200;
    --z-modal: 300;
    --z-tooltip: 400;
}

/* Usage */
.card {
    background: var(--color-bg);
    border: 1px solid var(--color-border);
    border-radius: var(--radius-md);
    padding: var(--space-lg);
    box-shadow: var(--shadow-md);
    transition: all var(--transition-normal);
}

.card:hover {
    box-shadow: var(--shadow-lg);
    transform: translateY(-2px);
}

/* Dark mode with variables */
@media (prefers-color-scheme: dark) {
    :root {
        --color-text: #ecf0f1;
        --color-bg: #1a1a2e;
        --color-bg-alt: #16213e;
        --color-border: #444;
    }
}

/* Fallback values */
.element {
    color: var(--color-primary, #3498db);
    padding: var(--spacing, 16px);
}
```

---

## Animations and Transitions

### Transitions

```css
/* Basic transition */
.button {
    background: var(--color-primary);
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: var(--radius-md);
    transition: background-color 0.3s ease;
}

.button:hover {
    background: var(--color-primary-dark);
}

/* Multiple properties */
.card {
    transition: transform 0.3s ease,
                box-shadow 0.3s ease,
                opacity 0.2s ease;
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: var(--shadow-lg);
}

/* Transition shorthand */
.element {
    transition: property duration timing-function delay;
    transition: all 0.3s ease 0s;
}

/* Timing functions */
.ease { transition-timing-function: ease; }
.linear { transition-timing-function: linear; }
.ease-in { transition-timing-function: ease-in; }
.ease-out { transition-timing-function: ease-out; }
.ease-in-out { transition-timing-function: ease-in-out; }
.cubic { transition-timing-function: cubic-bezier(0.68, -0.55, 0.265, 1.55); }
```

### Keyframe Animations

```css
/* Fade in */
@keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
}

/* Slide in from left */
@keyframes slideInLeft {
    from {
        opacity: 0;
        transform: translateX(-100px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

/* Bounce */
@keyframes bounce {
    0%, 20%, 53%, 80%, 100% {
        transform: translateY(0);
    }
    40%, 43% {
        transform: translateY(-20px);
    }
    70% {
        transform: translateY(-10px);
    }
    90% {
        transform: translateY(-4px);
    }
}

/* Spin */
@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

/* Pulse */
@keyframes pulse {
    0% { transform: scale(1); }
    50% { transform: scale(1.05); }
    100% { transform: scale(1); }
}

/* Shimmer loading effect */
@keyframes shimmer {
    0% {
        background-position: -200% 0;
    }
    100% {
        background-position: 200% 0;
    }
}

.loading {
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    animation: shimmer 1.5s infinite;
}

/* Applying animations */
.spinner {
    animation: spin 1s linear infinite;
}

.fade-in {
    animation: fadeIn 0.5s ease forwards;
}

.slide-in {
    animation: slideInLeft 0.5s ease forwards;
}

/* Animation properties */
.animated-element {
    animation: bounce 1s ease infinite;
    animation-name: bounce;
    animation-duration: 1s;
    animation-timing-function: ease;
    animation-delay: 0s;
    animation-iteration-count: infinite;
    animation-direction: normal;
    animation-fill-mode: forwards;
    animation-play-state: running;
}

/* Reduced motion */
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
    }
}
```

---

## Responsive Design

### Media Queries

```css
/* Mobile first approach */
.element {
    padding: 10px;  /* mobile default */
}

/* Tablet */
@media (min-width: 768px) {
    .element {
        padding: 20px;
    }
}

/* Desktop */
@media (min-width: 1024px) {
    .element {
        padding: 30px;
    }
}

/* Multiple conditions */
@media (min-width: 768px) and (max-width: 1024px) {
    .element {
        display: block;
    }
}

/* Orientation */
@media (orientation: landscape) {
    .hero { height: 50vh; }
}

@media (orientation: portrait) {
    .hero { height: 80vh; }
}

/* Dark mode */
@media (prefers-color-scheme: dark) {
    body {
        background: #1a1a1a;
        color: #fff;
    }
}
```

### Fluid Typography

```css
/* Clamp-based fluid typography */
h1 {
    font-size: clamp(1.5rem, 4vw, 3rem);
}

body {
    font-size: clamp(0.875rem, 1.5vw, 1rem);
}

/* Responsive without media queries */
.container {
    width: min(90%, 1200px);
    margin: 0 auto;
    padding: clamp(1rem, 5vw, 3rem);
}

/* Fluid spacing */
.section {
    padding: clamp(2rem, 8vw, 6rem) clamp(1rem, 4vw, 3rem);
}
```

### Responsive Grid

```css
/* Auto-fit responsive grid */
.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
}

/* Container queries */
.card-container {
    container-type: inline-size;
}

@container (min-width: 400px) {
    .card {
        display: grid;
        grid-template-columns: 200px 1fr;
    }
}
```

---

## Typography

```css
body {
    font-family: var(--font-family);
    font-size: var(--font-size-base);
    line-height: 1.6;
    color: var(--color-text);
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}

h1, h2, h3, h4, h5, h6 {
    font-weight: var(--font-weight-bold);
    line-height: 1.2;
    margin-bottom: 0.5em;
}

h1 { font-size: clamp(2rem, 5vw, 3.5rem); }
h2 { font-size: clamp(1.5rem, 4vw, 2.5rem); }
h3 { font-size: clamp(1.25rem, 3vw, 2rem); }

/* Text utilities */
.text-center { text-align: center; }
.text-right { text-align: right; }
.text-uppercase { text-transform: uppercase; }
.text-truncate {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

/* Multi-line truncation */
.line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* Responsive text */
@media (max-width: 768px) {
    body {
        font-size: 14px;
    }
}
```

---

## Positioning

```css
/* Static (default) */
.static {
    position: static;
}

/* Relative - offsets from normal position */
.relative {
    position: relative;
    top: 10px;
    left: 20px;
}

/* Absolute - relative to nearest positioned ancestor */
.absolute {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
}

/* Centering with absolute */
.center-absolute {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

/* Fixed - relative to viewport */
.fixed-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: var(--z-sticky);
}

/* Sticky - toggles between relative and fixed */
.sticky-nav {
    position: sticky;
    top: 0;
    z-index: var(--z-sticky);
}

/* Z-index layering */
.dropdown { z-index: 100; }
.modal-overlay { z-index: 200; }
.modal { z-index: 300; }
.tooltip { z-index: 400; }
```

---

## Modern CSS Features

### Container Queries

```css
.sidebar {
    container-type: inline-size;
    container-name: sidebar;
}

@container sidebar (min-width: 300px) {
    .widget {
        display: flex;
        gap: 16px;
    }
}
```

### :has() Selector

```css
/* Parent selector */
.card:has(img) {
    display: grid;
    grid-template-columns: 200px 1fr;
}

/* Form validation styling */
.form-group:has(input:invalid) {
    border-color: red;
}

/* Conditional styling based on children */
.list:has(li:nth-child(n+6)) {
    columns: 2;
}
```

### Color Functions

```css
.element {
    /* Modern color formats */
    color: oklch(0.7 0.15 250);
    background: oklch(0.9 0.05 120);

    /* Color mixing */
    border-color: color-mix(in srgb, var(--color-primary), white 30%);

    /* Relative color syntax */
    --color-primary-light: oklch(from var(--color-primary) calc(l + 0.2) c h);

    /* Light-dark() */
    color: light-dark(#1a1a1a, #ffffff);
    background: light-dark(#ffffff, #1a1a1a);
}
```

### Scroll-Driven Animations

```css
.progress-bar {
    position: fixed;
    top: 0;
    left: 0;
    height: 3px;
    background: var(--color-primary);
    transform-origin: left;
    animation: grow-progress linear;
    animation-timeline: scroll();
}

@keyframes grow-progress {
    from { transform: scaleX(0); }
    to { transform: scaleX(1); }
}

/* View timeline */
.reveal {
    animation: reveal linear;
    animation-timeline: view();
    animation-range: entry 0% cover 50%;
}

@keyframes reveal {
    from { opacity: 0; transform: translateY(100px); }
    to { opacity: 1; transform: translateY(0); }
}
```

### Subgrid

```css
.parent-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
}

.child {
    display: grid;
    grid-template-columns: subgrid;
    grid-column: span 2;
}
```

### Cascade Layers

```css
@layer base, components, utilities;

@layer base {
    h1 { font-size: 2rem; }
}

@layer components {
    .card h1 { font-size: 1.5rem; }
}

@layer utilities {
    .text-lg { font-size: 1.25rem; }
}
```

---

## CSS Best Practices

1. Use `box-sizing: border-box` globally
2. Adopt a mobile-first responsive design approach
3. Use CSS custom properties for theming and consistency
4. Prefer `flex` and `grid` over floats and positioning
5. Use semantic class names (`.card`, not `.blue-box`)
6. Avoid `!important` — restructure selectors instead
7. Use `rem` for font sizes, `px` for borders
8. Implement `prefers-reduced-motion` for accessibility
9. Use modern color formats (oklch, color-mix)
10. Organize CSS with methodology (BEM, ITCSS, or utility-first)
