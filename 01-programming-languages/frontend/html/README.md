# HTML5 Complete Guide

## Table of Contents

1. [Document Structure](#document-structure)
2. [Semantic HTML](#semantic-html)
3. [Forms and Validation](#forms-and-validation)
4. [Tables](#tables)
5. [Media Elements](#media-elements)
6. [Accessibility (a11y)](#accessibility)
7. [Meta Tags and SEO](#meta-tags-and-seo)
8. [HTML APIs](#html-apis)

---

## Document Structure

Every HTML5 document starts with a doctype declaration and follows a structured hierarchy.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Page description for SEO">
    <title>Page Title</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header>
        <nav></nav>
    </header>
    <main>
        <article>
            <section></section>
        </article>
        <aside></aside>
    </main>
    <footer></footer>
</body>
</html>
```

Key elements:
- `<!DOCTYPE html>` — forces standards mode in all browsers
- `<html lang="en">` — declares the document language for screen readers and search engines
- `<meta charset="UTF-8">` — sets character encoding to support all Unicode characters
- `<meta name="viewport">` — essential for responsive design on mobile devices
- `<title>` — required element, appears in browser tabs and search results

---

## Semantic HTML

Semantic elements convey meaning about their content to both browsers and developers.

### Layout Semantics

| Element | Purpose |
|---------|---------|
| `<header>` | Introductory content or navigational aids |
| `<nav>` | Major navigation blocks |
| `<main>` | Dominant content of the `<body>` (only one per page) |
| `<article>` | Self-contained, independently distributable content |
| `<section>` | Thematic grouping of content with a heading |
| `<aside>` | Content tangentially related to surrounding content |
| `<footer>` | Footer for its nearest sectioning content or root |
| `<figure>` | Self-contained content like images, diagrams, code |
| `<figcaption>` | Caption for a `<figure>` element |
| `<details>` | Disclosure widget for toggling content visibility |
| `<summary>` | Summary for `<details>` element |

### Content-Level Semantics

```html
<!-- Inline semantics -->
<p>This is a <strong>important</strong> paragraph.</p>
<p>This is <em>emphasized</em> text.</p>
<p>Use <code>console.log()</code> for debugging.</p>
<p>Press <kbd>Ctrl</kbd> + <kbd>C</kbd> to copy.</p>
<mark>This text is highlighted</mark>
<time datetime="2026-08-04">August 4th, 2026</time>
<abbr title="World Wide Web">WWW</abbr>
<address>Contact: <a href="mailto:info@example.com">info@example.com</a></address>
```

### Why Semantics Matter

- **Accessibility**: Screen readers use semantic elements to navigate and announce content correctly
- **SEO**: Search engines parse semantic structure to rank and index pages
- **Maintainability**: Clear structure makes code easier to read and modify
- **Default Styling**: Browsers apply meaningful default styles to semantic elements

---

## Forms and Validation

HTML5 introduced native form validation, new input types, and improved UX.

### Input Types

```html
<!-- Text inputs -->
<input type="text" placeholder="Enter name">
<input type="email" required>
<input type="password" minlength="8">
<input type="search" placeholder="Search...">
<input type="url" pattern="https://.*">
<input type="tel" pattern="[0-9]{10}">
<input type="number" min="0" max="100" step="5">
<input type="range" min="0" max="100" value="50">

<!-- Date/time inputs -->
<input type="date">
<input type="time">
<input type="datetime-local">
<input type="month">
<input type="week">

<!-- Other inputs -->
<input type="color">
<input type="file" accept="image/*" multiple>
<input type="hidden" name="userId" value="12345">
<input type="checkbox" id="agree">
<input type="radio" name="plan" value="free">
<input type="radio" name="plan" value="pro">
```

### Input Attributes

```html
<!-- Validation attributes -->
<input required>
<input minlength="3" maxlength="50">
<input min="1" max="10">
<input pattern="[A-Za-z]{3}">
<input step="0.01">

<!-- UI attributes -->
<input placeholder="Enter value">
<input autofocus>
<input readonly>
<input disabled>
<input multiple>
<input list="suggestions">
<datalist id="suggestions">
    <option value="HTML">
    <option value="CSS">
    <option value="JavaScript">
</datalist>
```

### Full Form Example

```html
<form action="/submit" method="POST" novalidate id="registration">
    <fieldset>
        <legend>Personal Information</legend>

        <div>
            <label for="name">Full Name *</label>
            <input
                type="text"
                id="name"
                name="name"
                required
                minlength="2"
                autocomplete="name"
            >
        </div>

        <div>
            <label for="email">Email *</label>
            <input
                type="email"
                id="email"
                name="email"
                required
                autocomplete="email"
                aria-describedby="email-help"
            >
            <small id="email-help">We will never share your email.</small>
        </div>

        <div>
            <label for="password">Password *</label>
            <input
                type="password"
                id="password"
                name="password"
                required
                minlength="8"
                pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                aria-describedby="password-requirements"
            >
            <small id="password-requirements">
                Must contain uppercase, lowercase, and a number
            </small>
        </div>
    </fieldset>

    <fieldset>
        <legend>Preferences</legend>

        <div>
            <label for="plan">Select Plan</label>
            <select id="plan" name="plan" required>
                <option value="">-- Choose --</option>
                <optgroup label="Free Tier">
                    <option value="basic">Basic</option>
                </optgroup>
                <optgroup label="Paid Tier">
                    <option value="pro">Pro</option>
                    <option value="enterprise">Enterprise</option>
                </optgroup>
            </select>
        </div>

        <div>
            <label for="bio">Bio</label>
            <textarea
                id="bio"
                name="bio"
                rows="4"
                maxlength="500"
                placeholder="Tell us about yourself..."
            ></textarea>
        </div>

        <div>
            <label>
                <input type="checkbox" name="terms" required>
                I agree to the terms and conditions
            </label>
        </div>
    </fieldset>

    <button type="submit">Register</button>
    <button type="reset">Clear Form</button>
</form>
```

### Custom Validation (JavaScript)

```html
<form id="customForm">
    <input type="email" id="email" required>
    <span id="emailError" role="alert"></span>
    <button type="submit">Submit</button>
</form>

<script>
const form = document.getElementById('customForm');
const email = document.getElementById('email');
const error = document.getElementById('emailError');

form.addEventListener('submit', (e) => {
    e.preventDefault();

    if (email.validity.valueMissing) {
        error.textContent = 'Email is required';
    } else if (email.validity.typeMismatch) {
        error.textContent = 'Please enter a valid email';
    } else {
        error.textContent = '';
        form.submit();
    }
});
</script>
```

---

## Tables

HTML tables represent tabular data in a structured format.

```html
<table>
    <caption>Monthly Revenue Report</caption>
    <thead>
        <tr>
            <th scope="col">Month</th>
            <th scope="col">Revenue</th>
            <th scope="col">Expenses</th>
            <th scope="col">Profit</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <th scope="row">January</th>
            <td>$50,000</td>
            <td>$30,000</td>
            <td>$20,000</td>
        </tr>
        <tr>
            <th scope="row">February</th>
            <td>$55,000</td>
            <td>$32,000</td>
            <td>$23,000</td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <th scope="row">Total</th>
            <td>$105,000</td>
            <td>$62,000</td>
            <td>$43,000</td>
        </tr>
    </tfoot>
</table>
```

### Colspan and Rowspan

```html
<table>
    <thead>
        <tr>
            <th colspan="2">Sales Report</th>
        </tr>
        <tr>
            <th>Product</th>
            <th>Region</th>
            <th>Units Sold</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td rowspan="2">Widget A</td>
            <td>North</td>
            <td>150</td>
        </tr>
        <tr>
            <td>South</td>
            <td>200</td>
        </tr>
        <tr>
            <td rowspan="2">Widget B</td>
            <td>North</td>
            <td>100</td>
        </tr>
        <tr>
            <td>South</td>
            <td>120</td>
        </tr>
    </tbody>
</table>
```

---

## Media Elements

### Images

```html
<!-- Basic image -->
<img src="photo.jpg" alt="A sunset over the mountains" width="800" height="600">

<!-- Responsive images with srcset -->
<img
    src="photo-800.jpg"
    srcset="photo-400.jpg 400w, photo-800.jpg 800w, photo-1200.jpg 1200w"
    sizes="(max-width: 600px) 400px, (max-width: 1000px) 800px, 1200px"
    alt="Responsive landscape photo"
    loading="lazy"
>

<!-- Art direction with picture element -->
<picture>
    <source media="(min-width: 1024px)" srcset="wide.jpg">
    <source media="(min-width: 600px)" srcset="medium.jpg">
    <img src="narrow.jpg" alt="Art directed image">
</picture>

<!-- Figure with caption -->
<figure>
    <img src="chart.png" alt="Revenue growth chart 2024-2026">
    <figcaption>Figure 1: Quarterly revenue growth over 2 years</figcaption>
</figure>
```

### Video and Audio

```html
<!-- Video with multiple sources -->
<video controls width="640" height="360" poster="thumbnail.jpg" preload="metadata">
    <source src="video.mp4" type="video/mp4">
    <source src="video.webm" type="video/webm">
    <track kind="captions" src="captions.vtt" srclang="en" label="English" default>
    <track kind="subtitles" src="subtitles.vtt" srclang="es" label="Spanish">
    Your browser does not support the video tag.
</video>

<!-- Autoplay with loop (muted required for autoplay) -->
<video autoplay loop muted playsinline>
    <source src="background.mp4" type="video/mp4">
</video>

<!-- Audio -->
<audio controls preload="metadata">
    <source src="podcast.mp3" type="audio/mpeg">
    <source src="podcast.ogg" type="audio/ogg">
    Your browser does not support the audio element.
</audio>
```

### iframes and Embedded Content

```html
<!-- Responsive iframe -->
<div class="video-container">
    <iframe
        src="https://www.youtube.com/embed/VIDEO_ID"
        title="Video title"
        width="560"
        height="315"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowfullscreen
        loading="lazy"
    ></iframe>
</div>

<!-- Map -->
<map name="worldmap">
    <area shape="rect" coords="340,120,460,200" href="north-america.html" alt="North America">
    <area shape="circle" coords="200,150,50" href="europe.html" alt="Europe">
    <area shape="poly" coords="100,100,200,50,300,100,200,150" href="asia.html" alt="Asia">
</map>
<img src="world-map.jpg" usemap="#worldmap" alt="World map">
```

---

## Accessibility

### ARIA Roles and Attributes

```html
<!-- Landmark roles -->
<div role="banner">Header content</div>
<div role="navigation">Nav content</div>
<div role="main">Main content</div>
<div role="contentinfo">Footer content</div>

<!-- Interactive widgets -->
<button aria-label="Close dialog" aria-expanded="false" onclick="toggleMenu()">
    X
</button>

<div role="tablist" aria-label="Settings tabs">
    <button role="tab" aria-selected="true" aria-controls="panel1" id="tab1">
        General
    </button>
    <button role="tab" aria-selected="false" aria-controls="panel2" id="tab2">
        Security
    </button>
</div>

<div role="tabpanel" id="panel1" aria-labelledby="tab1">
    General settings content
</div>

<!-- Live regions for dynamic content -->
<div aria-live="polite" aria-atomic="true" id="status">
    <!-- Updated dynamically via JavaScript -->
    3 items in your cart
</div>

<!-- Form accessibility -->
<label for="search">Search</label>
<input
    type="search"
    id="search"
    role="searchbox"
    aria-describedby="search-hint"
    aria-required="true"
>
<span id="search-hint">Enter at least 3 characters</span>
```

### Keyboard Navigation

```html
<!-- Custom keyboard-accessible widget -->
<div
    role="button"
    tabindex="0"
    aria-pressed="false"
    onclick="toggleTheme()"
    onkeydown="handleKeyPress(event)"
>
    Toggle Dark Mode
</div>

<!-- Skip navigation link -->
<a href="#main-content" class="skip-link">Skip to main content</a>

<style>
.skip-link {
    position: absolute;
    top: -40px;
    left: 0;
    padding: 8px;
    background: #000;
    color: #fff;
    z-index: 100;
}
.skip-link:focus {
    top: 0;
}
</style>
```

### Accessibility Checklist

- All images have meaningful `alt` text (decorative images use `alt=""`)
- Form inputs have associated `<label>` elements
- Color contrast ratio meets WCAG AA (4.5:1 for text, 3:1 for large text)
- All interactive elements are keyboard accessible
- Page has a single `<h1>` and logical heading hierarchy
- Language is declared with `lang` attribute
- Tables use `<caption>`, `<th scope>`, and `<thead>`/`<tbody>`
- Dynamic content uses `aria-live` regions
- Focus order is logical and visible
- ARIA roles are used correctly and only when native HTML is insufficient

---

## Meta Tags and SEO

```html
<head>
    <!-- Character encoding -->
    <meta charset="UTF-8">

    <!-- Viewport -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- SEO -->
    <title>Page Title - Site Name</title>
    <meta name="description" content="Concise description under 160 characters">
    <meta name="keywords" content="html, web, development">
    <meta name="author" content="Author Name">
    <link rel="canonical" href="https://example.com/canonical-url">

    <!-- Open Graph (Facebook, LinkedIn) -->
    <meta property="og:title" content="Page Title">
    <meta property="og:description" content="Description for social sharing">
    <meta property="og:image" content="https://example.com/image.jpg">
    <meta property="og:url" content="https://example.com/page">
    <meta property="og:type" content="website">
    <meta property="og:site_name" content="Site Name">

    <!-- Twitter Card -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="Page Title">
    <meta name="twitter:description" content="Description">
    <meta name="twitter:image" content="https://example.com/image.jpg">

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="manifest" href="/site.webmanifest">
    <meta name="theme-color" content="#ffffff">

    <!-- Preconnect and preload -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preload" href="font.woff2" as="font" type="font/woff2" crossorigin>
</head>
```

---

## HTML APIs

### Drag and Drop

```html
<div
    draggable="true"
    ondragstart="handleDragStart(event)"
    ondragend="handleDragEnd(event)"
>
    Drag me
</div>

<div
    ondragover="event.preventDefault()"
    ondrop="handleDrop(event)"
>
    Drop here
</div>

<script>
function handleDragStart(e) {
    e.dataTransfer.setData('text/plain', e.target.id);
    e.target.classList.add('dragging');
}

function handleDrop(e) {
    e.preventDefault();
    const id = e.dataTransfer.getData('text/plain');
    const el = document.getElementById(id);
    e.target.appendChild(el);
}
</script>
```

### Web Storage

```html
<script>
// localStorage - persists across sessions
localStorage.setItem('theme', 'dark');
const theme = localStorage.getItem('theme');
localStorage.removeItem('theme');
localStorage.clear();

// sessionStorage - cleared when tab closes
sessionStorage.setItem('cart', JSON.stringify(items));
const cart = JSON.parse(sessionStorage.getItem('cart'));
</script>
```

### Geolocation

```html
<script>
navigator.geolocation.getCurrentPosition(
    (position) => {
        console.log(position.coords.latitude);
        console.log(position.coords.longitude);
    },
    (error) => {
        console.error(error.message);
    },
    {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 300000
    }
);
</script>
```

### Canvas Basics

```html
<canvas id="myCanvas" width="800" height="600"></canvas>
<script>
const canvas = document.getElementById('myCanvas');
const ctx = canvas.getContext('2d');

// Draw a rectangle
ctx.fillStyle = '#3498db';
ctx.fillRect(10, 10, 150, 100);

// Draw a circle
ctx.beginPath();
ctx.arc(400, 300, 50, 0, Math.PI * 2);
ctx.fillStyle = '#e74c3c';
ctx.fill();

// Draw text
ctx.font = '24px Arial';
ctx.fillStyle = '#2c3e50';
ctx.fillText('Hello Canvas!', 10, 50);
</script>
```

### Web Workers

```javascript
// main.js
const worker = new Worker('worker.js');
worker.postMessage({ data: largeArray });
worker.onmessage = (e) => {
    console.log('Result:', e.data);
};

// worker.js
self.onmessage = (e) => {
    const result = processLargeData(e.data);
    self.postMessage(result);
};
```

---

## HTML5 Elements Quick Reference

| Category | Elements |
|----------|----------|
| **Structural** | `<header>`, `<nav>`, `<main>`, `<article>`, `<section>`, `<aside>`, `<footer>` |
| **Text** | `<mark>`, `<time>`, `<abbr>`, `<data>`, `<wbr>` |
| **Forms** | `<input>`, `<select>`, `<textarea>`, `<datalist>`, `<output>`, `<progress>`, `<meter>` |
| **Media** | `<audio>`, `<video>`, `<source>`, `<track>`, `<picture>` |
| **Embedded** | `<iframe>`, `<embed>`, `<object>`, `<param>` |
| **Interactive** | `<details>`, `<summary>`, `<dialog>`, `<menu>` |
| **Table** | `<table>`, `<thead>`, `<tbody>`, `<tfoot>`, `<tr>`, `<th>`, `<td>`, `<caption>`, `<colgroup>` |
| **Scripting** | `<canvas>`, `<script>`, `<noscript>` |

---

## Best Practices

1. Always use semantic elements over generic `<div>` and `<span>`
2. Declare `lang` attribute on `<html>` for accessibility
3. Use `<meta name="viewport">` for responsive design
4. Provide meaningful `alt` text for all images
5. Use `<label>` elements associated with form inputs
6. Maintain a logical heading hierarchy (`h1` through `h6`)
7. Use `loading="lazy"` on images and iframes for performance
8. Validate HTML using the W3C validator
9. Use `type` attributes on scripts and stylesheets for clarity
10. Prefer `<button>` over `<div>` for interactive elements
