// JavaScript Template Literals

// Basic interpolation
const name = "Alice";
const age = 30;
const greeting = `Hello, ${name}! You are ${age} years old.`;

// Expression interpolation
const price = 10;
const tax = 0.08;
const total = `Total: $${(price * (1 + tax)).toFixed(2)}`;

// Multi-line strings
const html = `
  <div class="card">
    <h2>${name}</h2>
    <p>Age: ${age}</p>
  </div>
`;

// Nested template literals
const users = ["Alice", "Bob", "Charlie"];
const list = `
  <ul>
    ${users.map(user => `<li>${user}</li>`).join("")}
  </ul>
`;

// Conditional rendering
const isLoggedIn = true;
const authSection = `
  ${isLoggedIn 
    ? `<span>Welcome, ${name}</span>` 
    : `<a href="/login">Login</a>`}
`;

// Tagged Templates
function sql(strings, ...values) {
  return strings.reduce((result, str, i) => {
    const value = values[i];
    if (value === undefined) return result + str;
    const escaped = String(value).replace(/'/g, "''");
    return result + str + `'${escaped}'`;
  }, "");
}

const userId = 1;
const query = sql`SELECT * FROM users WHERE id = ${userId}`;

// Syntax highlighting example
function highlight(strings, ...values) {
  return strings.reduce((result, str, i) => {
    const value = values[i];
    if (value === undefined) return result + str;
    return result + str + `<span class="value">${value}</span>`;
  }, "");
}

const code = highlight`const x = ${42}; const y = ${"hello"};`;

// I18n (Internationalization)
function i18n(strings, ...values) {
  const locale = "en";
  return strings.reduce((result, str, i) => {
    const value = values[i];
    if (value === undefined) return result + str;
    if (typeof value === "number") {
      return result + str + new Intl.NumberFormat(locale).format(value);
    }
    return result + str + value;
  }, "");
}

const amount = 1234567.89;
const formatted = i18n`The amount is ${amount} dollars`;

// Security: Preventing injection
function safeHtml(strings, ...values) {
  return strings.reduce((result, str, i) => {
    const value = values[i];
    if (value === undefined) return result + str;
    const escaped = String(value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
    return result + str + escaped;
  }, "");
}

const userInput = '<script>alert("xss")</script>';
const safe = safeHtml`<p>${userInput}</p>`;

// Raw strings (preserve escape sequences)
function raw(strings, ...values) {
  return strings.raw.reduce((result, str, i) => {
    const value = values[i];
    if (value === undefined) return result + str;
    return result + str + value;
  }, "");
}

const path = raw`C:\Users\${name}\Documents`;
// "C:\\Users\\Alice\\Documents"

// Nested expressions
const data = [
  { name: "Alice", score: 95 },
  { name: "Bob", score: 87 }
];

const table = `
  <table>
    ${data.map(row => `
      <tr>
        <td>${row.name}</td>
        <td>${row.score > 90 ? "A" : row.score > 80 ? "B" : "C"}</td>
      </tr>
    `).join("")}
  </table>
`;
