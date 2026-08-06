"""if/elif/else, match/case (Python 3.10+)."""

# ── Basic if/elif/else ───────────────────────────────────────────────
score = 85

if score >= 90:
    grade = "A"
elif score >= 80:
    grade = "B"
elif score >= 70:
    grade = "C"
elif score >= 60:
    grade = "D"
else:
    grade = "F"

print(f"Score {score} → Grade {grade}")

# ── Ternary Expression ──────────────────────────────────────────────
age = 20
status = "adult" if age >= 18 else "minor"

# Nested ternary (avoid for readability)
x = "positive" if x > 0 else ("zero" if x == 0 else "negative")

# ── Truthy/Falsy Values ─────────────────────────────────────────────
# Falsy: False, None, 0, 0.0, "", [], {}, set(), frozenset(), b""
# Everything else is truthy

if []:                    # Empty list is falsy
    print("This won't run")

if [1, 2]:               # Non-empty list is truthy
    print("This runs")

# ── Match/Case (Python 3.10+) ───────────────────────────────────────
# Structural pattern matching

def handle_command(command):
    match command.split():
        case ["quit"]:
            return "Exiting..."
        case ["hello", name]:
            return f"Hello, {name}!"
        case ["add", *numbers]:
            total = sum(int(n) for n in numbers)
            return f"Sum: {total}"
        case _:
            return "Unknown command"

print(handle_command("hello Alice"))   # Hello, Alice!
print(handle_command("add 1 2 3"))    # Sum: 6

# ── Guard Clauses (Early Return) ────────────────────────────────────
def process_order(order):
    if not order:
        return "No order"
    if not order.get("items"):
        return "No items"
    if order.get("total", 0) <= 0:
        return "Invalid total"

    # Main logic at end — no nested ifs
    return f"Processing {len(order['items'])} items"

# ── Chained Comparisons ─────────────────────────────────────────────
x = 15
if 10 < x < 20:
    print("x is between 10 and 20")

# ── Walrus Operator := (Python 3.8+) ────────────────────────────────
# Assign and use in same expression
data = [1, 2, 3, 4, 5]
if (n := len(data)) > 3:
    print(f"List has {n} elements")
