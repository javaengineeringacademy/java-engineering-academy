# While Loop — The Unknown Counter

## Why While Loop Exists

Sometimes you don't know how many times you'll iterate. You're reading from a file, a network socket, or a user input — and the end condition is discovered at runtime. The `while` loop excels at these scenarios.

**Production incident:** A data ingestion pipeline used a `for` loop with a hardcoded upper bound to read CSV rows. When the file had more rows than expected, data was silently truncated. Switching to `while (scanner.hasNextLine())` fixed the data loss.

## The Pain Point

For loops require you to know the iteration count upfront:
```java
for (int i = 0; i < unknownSize; i++)  // What is unknownSize?
```

While loops iterate until a condition becomes false — the condition can be anything, discovered during iteration.

## Basic Syntax

```java
// while loop
while (condition) {
    // body — executes as long as condition is true
}

// do-while loop
do {
    // body — executes at least once
} while (condition);

// Key difference:
// while: checks condition BEFORE first execution
// do-while: checks condition AFTER first execution (guarantees one execution)
```

## Scanner / BufferedReader Patterns

```java
// Reading lines from file
Scanner scanner = new Scanner(new File("data.txt"));
while (scanner.hasNextLine()) {
    String line = scanner.nextLine();
    process(line);
}
scanner.close();

// Reading from BufferedReader (faster for large files)
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
String line;
while ((line = reader.readLine()) != null) {
    process(line);
}
reader.close();

// Try-with-resources (recommended)
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        process(line);
    }
}

// Reading from network socket
Socket socket = new Socket("host", 8080);
BufferedReader in = new BufferedReader(
    new InputStreamReader(socket.getInputStream()));
String input;
while ((input = in.readLine()) != null) {
    // Process server response
}
```

## Reading Until Sentinel Value

```java
// Classic sentinel pattern
Scanner scanner = new Scanner(System.in);
System.out.println("Enter numbers (negative to quit):");

List<Integer> numbers = new ArrayList<>();
while (true) {
    int value = scanner.nextInt();
    if (value < 0) break;  // Sentinel value
    numbers.add(value);
}

// Alternative: read until specific word
String input;
while (!(input = scanner.nextLine()).equals("QUIT")) {
    process(input);
}

// Read until empty line
String line;
while (!(line = scanner.nextLine()).isEmpty()) {
    process(line);
}
```

## Do-While vs While

```java
// Do-while: guaranteed execution
Scanner scanner = new Scanner(System.in);
String input;
do {
    System.out.print("Enter password: ");
    input = scanner.nextLine();
} while (!input.equals("secret"));

// While: might not execute at all
String input = scanner.nextLine();  // Read first
while (!input.equals("QUIT")) {
    process(input);
    input = scanner.nextLine();  // Read next
}

// Do-while is better for "at least once" patterns:
// - Menu display
// - Input validation
// - Retry logic
```

## Common Patterns

```java
// Pattern 1: Process until done
while (!queue.isEmpty()) {
    Task task = queue.poll();
    task.execute();
}

// Pattern 2: Retry with backoff
int attempts = 0;
while (attempts < MAX_RETRIES) {
    try {
        callExternalService();
        break;  // Success
    } catch (Exception e) {
        attempts++;
        Thread.sleep(1000 * attempts);  // Exponential backoff
    }
}

// Pattern 3: Polling
while (true) {
    Status status = checkStatus();
    if (status.isDone()) break;
    Thread.sleep(1000);
}

// Pattern 4: Reading until EOF
try (Scanner s = new Scanner(System.in)) {
    while (s.hasNext()) {
        String token = s.next();
        process(token);
    }
}
```

## When to Use / When NOT to Use

### ✅ USE While Loop When:
- Number of iterations is unknown
- Reading from streams/files/user input
- Sentinel value termination
- Retry logic with backoff
- Menu-driven programs
- Polling patterns

### ❌ DON'T Use While Loop When:
- You know the iteration count → use `for`
- Iterating a Collection → use enhanced for
- Need index access → use `for`
- Simple traversal → use enhanced for or Stream

## Common Mistakes

### Mistake 1: Infinite Loop
```java
// WRONG: missing update statement
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Forgot: i++!
}
// Infinite loop!

// RIGHT:
int i = 0;
while (i < 10) {
    System.out.println(i);
    i++;
}

// ALWAYS ensure the condition eventually becomes false
```

### Mistake 2: Off-by-One in Do-While
```java
// WRONG: processes sentinel value
do {
    input = scanner.nextLine();
    process(input);  // Processes "QUIT"!
} while (!input.equals("QUIT"));

// RIGHT: check before processing
do {
    input = scanner.nextLine();
    if (!input.equals("QUIT")) {
        process(input);
    }
} while (!input.equals("QUIT"));
```

### Mistake 3: Exception Skipping Update
```java
// WRONG: if exception thrown, update is skipped
while (hasNext()) {
    Data d = readData();  // Might throw
    process(d);
    advance();  // Skipped if readData() throws
}

// RIGHT: try-finally or try-with-resources
while (hasNext()) {
    try {
        Data d = readData();
        process(d);
    } finally {
        advance();  // Always executes
    }
}
```

### Mistake 4: Confusing While with Do-While
```java
// When user enters invalid data first, while doesn't process it
String input = scanner.nextLine();  // What if first input is valid?
while (!input.equals("QUIT")) {
    process(input);
    input = scanner.nextLine();
}

// Do-while processes first input
do {
    String input = scanner.nextLine();
    if (!input.equals("QUIT")) process(input);
} while (!input.equals("QUIT"));  // Variable scope issue!
// Actually, input goes out of scope — fix:
String input;
do {
    input = scanner.nextLine();
    if (!input.equals("QUIT")) process(input);
} while (!input.equals("QUIT"));
```

## Performance

```
Pattern             │ Time    │ Notes
────────────────────┼─────────┼──────────────────────
while (file.read()) │ O(n)    │ Depends on read cost
while (queue.poll())│ O(n)    │ One iteration per element
do-while (input)    │ O(1)+   │ At least one iteration
```

While loop performance is identical to for loop — both are O(n) when iterating a collection. The difference is semantic, not performance.

## Interview Questions

**Q: When would you use while over for loop?**
A: When the number of iterations is unknown at the start — reading files, user input, sentinel values.

**Q: What's the difference between while and do-while?**
A: While checks condition before execution (may not execute). Do-while executes body first, then checks (guarantees at least one execution).

**Q: How do you prevent infinite loops?**
A: Ensure the loop body modifies the condition variable. Use timeouts or max iteration counts for safety.

**Q: Can while loop iterate a Collection?**
A: Yes, but you'd need an Iterator. Enhanced for is cleaner for Collection iteration.

**Q: Why is try-finally important in while loops reading resources?**
A: Without finally, an exception in the body can skip the "advance" step, causing an infinite loop or resource leak.
