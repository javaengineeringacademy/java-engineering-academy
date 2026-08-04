# Accessibility (a11y)

Accessibility ensures that websites and applications are usable by everyone, including people with disabilities. It's not just a feature—it's a fundamental requirement.

## Table of Contents

- [WCAG 2.1 Guidelines](#wcag-21-guidelines)
- [Semantic HTML](#semantic-html)
- [ARIA](#aria)
- [Keyboard Navigation](#keyboard-navigation)
- [Focus Management](#focus-management)
- [Screen Readers](#screen-readers)
- [Color & Contrast](#color--contrast)
- [Alt Text](#alt-text)
- [Form Labels](#form-labels)
- [Testing Tools](#testing-tools)

---

## WCAG 2.1 Guidelines

The Web Content Accessibility Guidelines (WCAG) define how to make web content more accessible:

### The Four Principles (POUR)

1. **Perceivable** - Information must be presentable in ways users can perceive
2. **Operable** - Interface components must be operable by all users
3. **Understandable** - Information and UI operation must be understandable
4. **Robust** - Content must be robust enough for assistive technologies

### Conformance Levels

| Level | Description |
|-------|-------------|
| A | Minimum level - essential for accessibility |
| AA | Recommended - addresses most barriers |
| AAA | Highest level - comprehensive accessibility |

### Success Criteria Examples

```html
<!-- 1.1.1 Non-text Content (Level A) -->
<img src="chart.png" alt="Sales increased 25% from Q1 to Q2 2024" />

<!-- 1.3.1 Info and Relationships (Level A) -->
<table>
  <caption>Quarterly Sales Data</caption>
  <thead>
    <tr>
      <th scope="col">Quarter</th>
      <th scope="col">Revenue</th>
    </tr>
  </thead>
</table>

<!-- 1.4.3 Contrast Minimum (Level AA) -->
<!-- Text must have contrast ratio of at least 4.5:1 -->
<style>
  .text {
    color: #333333; /* Foreground */
    background-color: #ffffff; /* Background */
    /* Ratio: 12.63:1 ✓ */
  }
</style>

<!-- 2.1.1 Keyboard (Level A) -->
<button onclick="submit()" onkeypress="handleKeyPress(event)">
  Submit
</button>

<!-- 2.4.1 Bypass Blocks (Level A) -->
<a href="#main-content" class="skip-link">Skip to main content</a>

<!-- 2.4.7 Focus Visible (Level AA) -->
<style>
  :focus {
    outline: 2px solid #005fcc;
    outline-offset: 2px;
  }
</style>
```

---

## Semantic HTML

Use HTML elements according to their intended purpose:

```html
<!-- Bad: Non-semantic -->
<div class="header">
  <div class="nav">
    <div class="nav-item"><div onclick="goToHome()">Home</div></div>
  </div>
</div>
<div class="content">
  <div class="article">
    <div class="title">Article Title</div>
    <div class="text">Article content...</div>
  </div>
</div>

<!-- Good: Semantic HTML -->
<header>
  <nav aria-label="Main navigation">
    <ul>
      <li><a href="/">Home</a></li>
      <li><a href="/about">About</a></li>
    </ul>
  </nav>
</header>
<main id="main-content">
  <article>
    <h1>Article Title</h1>
    <p>Article content...</p>
  </article>
</main>
<footer>
  <p>© 2024 My Company</p>
</footer>
```

### Landmark Elements

```html
<header>    <!-- Banner landmark -->
<nav>       <!-- Navigation landmark -->
<main>      <!-- Main content landmark -->
<aside>     <!-- Complementary landmark -->
<footer>    <!-- Contentinfo landmark -->
<form>      <!-- Form landmark -->
<section>   <!-- Region landmark (with accessible name) -->
<article>   <!-- Article landmark -->
```

### Heading Hierarchy

```html
<!-- Maintain proper heading hierarchy -->
<h1>Page Title</h1>
<h2>Section 1</h2>
<h3>Subsection 1.1</h3>
<h3>Subsection 1.2</h3>
<h2>Section 2</h2>
<h3>Subsection 2.1</h3>
```

---

## ARIA

ARIA (Accessible Rich Internet Applications) attributes enhance accessibility:

### Roles

```html
<!-- Landmark roles -->
<div role="banner">...</div>
<div role="navigation">...</div>
<div role="main">...</div>
<div role="complementary">...</div>
<div role="contentinfo">...</div>
<div role="search">...</div>

<!-- Widget roles -->
<div role="button">Click me</div>
<div role="tab">Tab 1</div>
<div role="tabpanel">Tab content</div>
<div role="tablist">
  <div role="tab">Tab 1</div>
  <div role="tab">Tab 2</div>
</div>
<div role="dialog" aria-label="Confirm">Are you sure?</div>
<div role="alert">Error occurred!</div>
<div role="status">Loading...</div>

<!-- Structure roles -->
<div role="list">
  <div role="listitem">Item 1</div>
  <div role="listitem">Item 2</div>
</div>
<div role="table">
  <div role="row">
    <div role="cell">Cell 1</div>
    <div role="cell">Cell 2</div>
  </div>
</div>
```

### Properties

```html
<!-- Labels and descriptions -->
<input aria-label="Search">
<input aria-labelledby="label-id">
<div aria-describedby="help-text">Email address</div>
<div id="help-text">We'll never share your email</div>

<!-- Required and invalid -->
<input aria-required="true">
<input aria-invalid="true" aria-errormessage="error-msg">

<!-- Current state -->
<nav aria-current="page"><a href="/current">Current Page</a></nav>
<div aria-expanded="false">Dropdown</div>

<!-- Relationships -->
<div role="tab" aria-controls="panel-1">Tab 1</div>
<div id="panel-1" role="tabpanel" aria-labelledby="tab-1">Content</div>

<label id="color-label">Color</label>
<select aria-labelledby="color-label">
  <option>Red</option>
</select>
```

### States

```html
<!-- Button states -->
<button aria-pressed="true">Toggle On</button>
<button aria-pressed="false">Toggle Off</button>

<!-- Checkbox states -->
<input type="checkbox" aria-checked="true">
<input type="checkbox" aria-checked="false">
<input type="checkbox" aria-checked="mixed">

<!-- Disabled state -->
<button disabled aria-disabled="true">Disabled</button>

<!-- Hidden state -->
<div aria-hidden="true">Hidden from screen readers</div>

<!-- Live regions -->
<div aria-live="polite">Updates politely</div>
<div aria-live="assertive">Updates immediately</div>
<div aria-atomic="true">Entire region announced</div>
```

---

## Keyboard Navigation

Ensure all functionality is accessible via keyboard:

```html
<!-- Focusable elements -->
<a href="/link">Link</a>
<button>Button</button>
<input type="text" />
<select><option>Option</option></select>
<textarea></textarea>
<div tabindex="0">Focusable div</div>
<div tabindex="-1">Programmatically focusable</div>

<!-- Skip navigation -->
<a href="#main-content" class="skip-link">
  Skip to main content
</a>

<style>
  .skip-link {
    position: absolute;
    top: -40px;
    left: 0;
    background: #000;
    color: #fff;
    padding: 8px;
    z-index: 100;
  }

  .skip-link:focus {
    top: 0;
  }
</style>
```

### Custom Keyboard Handlers

```typescript
// React component with keyboard navigation
function Dropdown({ items, onSelect }) {
  const [isOpen, setIsOpen] = useState(false);
  const [activeIndex, setActiveIndex] = useState(-1);
  const listRef = useRef<HTMLUListElement>(null);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    switch (e.key) {
      case "ArrowDown":
        e.preventDefault();
        setActiveIndex((prev) =>
          prev < items.length - 1 ? prev + 1 : prev
        );
        break;
      case "ArrowUp":
        e.preventDefault();
        setActiveIndex((prev) => (prev > 0 ? prev - 1 : prev));
        break;
      case "Enter":
      case " ":
        e.preventDefault();
        if (activeIndex >= 0) {
          onSelect(items[activeIndex]);
          setIsOpen(false);
        }
        break;
      case "Escape":
        setIsOpen(false);
        break;
      case "Home":
        e.preventDefault();
        setActiveIndex(0);
        break;
      case "End":
        e.preventDefault();
        setActiveIndex(items.length - 1);
        break;
    }
  };

  return (
    <div>
      <button
        onClick={() => setIsOpen(!isOpen)}
        aria-expanded={isOpen}
        aria-haspopup="listbox"
      >
        Select option
      </button>
      {isOpen && (
        <ul
          ref={listRef}
          role="listbox"
          aria-activedescendant={
            activeIndex >= 0 ? `option-${activeIndex}` : undefined
          }
          onKeyDown={handleKeyDown}
        >
          {items.map((item, index) => (
            <li
              key={item.id}
              id={`option-${index}`}
              role="option"
              aria-selected={index === activeIndex}
              onClick={() => {
                onSelect(item);
                setIsOpen(false);
              }}
            >
              {item.label}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
```

---

## Focus Management

Control focus for dynamic content:

```typescript
// Focus trap for modals
function Modal({ isOpen, onClose, children }) {
  const modalRef = useRef<HTMLDivElement>(null);
  const previousFocusRef = useRef<HTMLElement | null>(null);

  useEffect(() => {
    if (isOpen) {
      previousFocusRef.current = document.activeElement as HTMLElement;
      modalRef.current?.focus();
    } else {
      previousFocusRef.current?.focus();
    }
  }, [isOpen]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Escape") {
      onClose();
      return;
    }

    if (e.key === "Tab") {
      const focusableElements = modalRef.current?.querySelectorAll(
        'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
      );

      if (!focusableElements?.length) return;

      const firstElement = focusableElements[0] as HTMLElement;
      const lastElement = focusableElements[focusableElements.length - 1] as HTMLElement;

      if (e.shiftKey && document.activeElement === firstElement) {
        e.preventDefault();
        lastElement.focus();
      } else if (!e.shiftKey && document.activeElement === lastElement) {
        e.preventDefault();
        firstElement.focus();
      }
    }
  };

  if (!isOpen) return null;

  return (
    <div
      ref={modalRef}
      role="dialog"
      aria-modal="true"
      aria-label="Modal dialog"
      tabIndex={-1}
      onKeyDown={handleKeyDown}
    >
      {children}
      <button onClick={onClose} aria-label="Close modal">
        ×
      </button>
    </div>
  );
}

// Announce route changes
function RouteAnnouncer() {
  const [announcement, setAnnouncement] = useState("");
  const location = useLocation();

  useEffect(() => {
    const pageTitle = document.title;
    setAnnouncement(`Navigated to ${pageTitle}`);
  }, [location]);

  return (
    <div
      role="status"
      aria-live="polite"
      aria-atomic="true"
      className="sr-only"
    >
      {announcement}
    </div>
  );
}
```

---

## Screen Readers

Support assistive technologies:

```html
<!-- Visually hidden text for screen readers -->
<style>
  .sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
  }
</style>

<span class="sr-only">Close dialog</span>

<!-- ARIA labels for icon buttons -->
<button aria-label="Close dialog">
  <svg aria-hidden="true">
    <path d="..." />
  </svg>
</button>

<!-- Live regions for dynamic updates -->
<div aria-live="polite" aria-atomic="true">
  {notification && <p>{notification}</p>}
</div>

<!-- Loading announcements -->
<div aria-live="polite" aria-busy={isLoading}>
  {isLoading ? (
    <span>Loading data, please wait...</span>
  ) : (
    <span>Data loaded successfully</span>
  )}
</div>

<!-- Image descriptions -->
<figure>
  <img
    src="chart.png"
    alt="Bar chart showing sales growth from $1M in Q1 to $1.5M in Q4 2024"
  />
  <figcaption>Figure 1: Quarterly sales performance</figcaption>
</figure>

<!-- Complex data tables -->
<table>
  <caption>
    <span class="sr-only">Quarterly sales data for 2024</span>
  </caption>
  <thead>
    <tr>
      <th scope="col">Quarter</th>
      <th scope="col">Revenue</th>
      <th scope="col">Growth</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">Q1</th>
      <td>$1,000,000</td>
      <td>+10%</td>
    </tr>
  </tbody>
</table>
```

---

## Color & Contrast

Ensure sufficient color contrast and don't rely on color alone:

```css
/* Minimum contrast ratios:
   - Normal text: 4.5:1
   - Large text (18px+): 3:1
   - UI components: 3:1 */

/* Good contrast example */
.text-high-contrast {
  color: #1a1a1a; /* Dark text */
  background-color: #ffffff; /* Light background */
  /* Contrast ratio: 16.75:1 ✓ */
}

.text-normal {
  color: #595959; /* Medium gray */
  background-color: #ffffff;
  /* Contrast ratio: 7.0:1 ✓ */
}

/* Error states - don't rely on color alone */
.error {
  color: #d32f2f;
  border-left: 4px solid #d32f2f; /* Visual indicator */
}

.error::before {
  content: "Error: "; /* Text indicator */
}

/* Form validation */
.input-error {
  border: 2px solid #d32f2f;
  box-shadow: 0 0 0 1px #d32f2f;
}

.input-error::after {
  content: "Required field";
  color: #d32f2f;
  font-size: 0.875rem;
}

/* Focus indicators */
:focus {
  outline: 2px solid #005fcc;
  outline-offset: 2px;
}

:focus:not(:focus-visible) {
  outline: none;
}

:focus-visible {
  outline: 2px solid #005fcc;
  outline-offset: 2px;
}

/* Don't disable focus for keyboard users */
.disabled {
  opacity: 0.5;
  pointer-events: none;
}
```

---

## Alt Text

Write meaningful alternative text for images:

```html
<!-- Informative images -->
<img src="team-photo.jpg" alt="Our team of 10 developers at the 2024 company retreat" />

<!-- Decorative images (empty alt) -->
<img src="divider.png" alt="" role="presentation" />

<!-- Complex images with long description -->
<figure>
  <img src="infographic.png" alt="Sales performance infographic" aria-describedby="infographic-desc" />
  <figcaption id="infographic-desc">
    Sales increased 25% year-over-year, with Q4 showing the strongest
    growth at 35%. The marketing channel drove 60% of new customers.
  </figcaption>
</figure>

<!-- Images in links -->
<a href="/products">
  <img src="products.jpg" alt="View all products" />
</a>

<!-- Icons with accessible names -->
<button aria-label="Delete item">
  <svg aria-hidden="true" focusable="false">
    <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12z" />
  </svg>
</button>

<!-- SVG with title -->
<svg role="img" aria-labelledby="chart-title">
  <title id="chart-title">Sales Growth Chart</title>
  <desc>A line chart showing upward sales trend</desc>
  <!-- chart elements -->
</svg>
```

---

## Form Labels

Properly associate labels with form controls:

```html
<!-- Explicit label (preferred) -->
<label for="email">Email Address</label>
<input type="email" id="email" name="email" autocomplete="email" />

<!-- Implicit label -->
<label>
  Email Address
  <input type="email" name="email" />
</label>

<!-- Required fields -->
<label for="password">
  Password <span aria-hidden="true">*</span>
  <span class="sr-only">(required)</span>
</label>
<input
  type="password"
  id="password"
  name="password"
  required
  aria-required="true"
  autocomplete="new-password"
/>

<!-- Fieldset and legend for grouped controls -->
<fieldset>
  <legend>Preferred contact method</legend>
  <div>
    <input type="radio" id="contact-email" name="contact" value="email" />
    <label for="contact-email">Email</label>
  </div>
  <div>
    <input type="radio" id="contact-phone" name="contact" value="phone" />
    <label for="contact-phone">Phone</label>
  </div>
</fieldset>

<!-- Error messages -->
<label for="username">Username</label>
<input
  type="text"
  id="username"
  aria-describedby="username-error username-help"
  aria-invalid="true"
  aria-errormessage="username-error"
/>
<span id="username-help">Must be at least 3 characters</span>
<span id="username-error" role="alert">
  Username must be at least 3 characters long
</span>

<!-- Select with label -->
<label for="country">Country</label>
<select id="country" name="country" autocomplete="country">
  <option value="">Select a country</option>
  <option value="us">United States</option>
  <option value="uk">United Kingdom</option>
</select>

<!-- Textarea with label -->
<label for="bio">Bio</label>
<textarea
  id="bio"
  name="bio"
  maxlength="500"
  aria-describedby="bio-count"
></textarea>
<span id="bio-count" aria-live="polite">0/500 characters</span>
```

---

## Testing Tools

Tools for testing accessibility:

### Automated Testing

```typescript
// jest-axe for unit tests
import { axe, toHaveNoViolations } from "jest-axe";

expect.extend(toHaveNoViolations);

describe("Button", () => {
  it("should have no accessibility violations", async () => {
    const { container } = render(<Button>Click me</Button>);
    const results = await axe(container);
    expect(results).toHaveNoViolations();
  });
});

// React Testing Library with accessibility queries
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

describe("LoginForm", () => {
  it("has accessible labels", () => {
    render(<LoginForm />);

    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /sign in/i })).toBeInTheDocument();
  });

  it("announces errors", async () => {
    render(<LoginForm />);

    await userEvent.click(screen.getByRole("button", { name: /sign in/i }));

    expect(screen.getByRole("alert")).toBeInTheDocument();
    expect(screen.getByText(/email is required/i)).toBeInTheDocument();
  });
});
```

### Browser Extensions

- **axe DevTools**: Comprehensive accessibility testing
- **WAVE**: Visual accessibility evaluation
- **Lighthouse**: Performance and accessibility audits
- **Color Contrast Analyzer**: Check color contrast ratios

### Screen Reader Testing

- **NVDA**: Free Windows screen reader
- **VoiceOver**: Built-in macOS/iOS screen reader
- **JAWS**: Windows screen reader
- **TalkBack**: Android screen reader

### Command Line Tools

```bash
# axe-core CLI
npx axe-cli https://example.com

# pa11y
npx pa11y https://example.com

# Lighthouse
npx lighthouse https://example.com --only-categories=accessibility

# eslint-plugin-jsx-a11y
npm install eslint-plugin-jsx-a11y --save-dev
```

```javascript
// .eslintrc.js
module.exports = {
  extends: ["plugin:jsx-a11y/recommended"],
  plugins: ["jsx-a11y"],
  rules: {
    "jsx-a11y/anchor-is-valid": "error",
    "jsx-a11y/click-events-have-key-events": "error",
    "jsx-a11y/no-static-element-interactions": "error",
  },
};
```
