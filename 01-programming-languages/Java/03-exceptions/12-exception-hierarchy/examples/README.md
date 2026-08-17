# Examples: Exception Hierarchy in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Throwable Hierarchy

```java
public class HierarchyOverview {
    public static void main(String[] args) {
        System.out.println("Java Exception Hierarchy:");
        System.out.println("  Throwable");
        System.out.println("  ├── Error (unchecked)");
        System.out.println("  │   ├── OutOfMemoryError");
        System.out.println("  │   ├── StackOverflowError");
        System.out.println("  │   └── AssertionError");
        System.out.println("  └── Exception (checked)");
        System.out.println("      ├── IOException");
        System.out.println("      ├── SQLException");
        System.out.println("      └── RuntimeException (unchecked)");
        System.out.println("          ├── NullPointerException");
        System.out.println("          ├── IllegalArgumentException");
        System.out.println("          └── IllegalStateException");
    }
}
```

**Output:**
```
Java Exception Hierarchy:
  Throwable
  ├── Error (unchecked)
  │   ├── OutOfMemoryError
  │   ├── StackOverflowError
  │   └── AssertionError
  └── Exception (checked)
      ├── IOException
      ├── SQLException
      └── RuntimeException (unchecked)
          ├── NullPointerException
          ├── IllegalArgumentException
          └── IllegalStateException
```

**Explanation:** The hierarchy determines catch behavior. Catching `Exception` catches all exceptions (including unchecked). Catching `RuntimeException` catches only unchecked exceptions. Catching `IOException` catches only that specific type.

---

## Example 2: Catch Order Matters

```java
public class CatchOrder {
    public static void main(String[] args) {
        try {
            throw new NullPointerException("null ref");
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Caught RuntimeException: null ref
```

**Explanation:** Catch blocks are checked in order. `NullPointerException` is a `RuntimeException`, so the first catch handles it. If the order were reversed, `Exception` would catch everything, making the `RuntimeException` catch unreachable (compiler error).

---

## Example 3: Polymorphic Catch

```java
public class PolymorphicCatch {
    static void handleException(Throwable t) {
        if (t instanceof Error) {
            System.out.println("Fatal: " + t.getMessage());
        } else if (t instanceof RuntimeException) {
            System.out.println("Runtime: " + t.getMessage());
        } else if (t instanceof Exception) {
            System.out.println("Checked: " + t.getMessage());
        }
    }

    public static void main(String[] args) {
        handleException(new OutOfMemoryError("heap full"));
        handleException(new NullPointerException("null"));
        handleException(new java.io.IOException("disk error"));
    }
}
```

**Output:**
```
Fatal: heap full
Runtime: null
Checked: disk error
```

**Explanation:** Instanceof checks navigate the hierarchy. `Error` is checked first because it's the most severe. `RuntimeException` is checked before `Exception` to distinguish checked from unchecked. This pattern allows handling different exception types differently.

---

## Example 4: Custom Hierarchy

```java
public class CustomHierarchy {
    static class AppException extends Exception {
        AppException(String msg) { super(msg); }
        AppException(String msg, Throwable cause) { super(msg, cause); }
    }

    static class ValidationException extends AppException {
        private final String field;
        ValidationException(String field, String msg) { super(msg); this.field = field; }
        String getField() { return field; }
    }

    static class NotFoundException extends AppException {
        private final String id;
        NotFoundException(String id) { super("Not found: " + id); this.id = id; }
        String getId() { return id; }
    }

    static class PermissionException extends AppException {
        private final String action;
        PermissionException(String action) { super("No permission for: " + action); this.action = action; }
        String getAction() { return action; }
    }

    public static void main(String[] args) {
        AppException[] exceptions = {
            new ValidationException("email", "Invalid format"),
            new NotFoundException("USR-456"),
            new PermissionException("delete")
        };

        for (AppException e : exceptions) {
            try {
                throw e;
            } catch (ValidationException ve) {
                System.out.println("Validation [" + ve.getField() + "]: " + ve.getMessage());
            } catch (NotFoundException nfe) {
                System.out.println("Not found: " + nfe.getId());
            } catch (PermissionException pe) {
                System.out.println("Permission: " + pe.getAction());
            } catch (AppException ae) {
                System.out.println("General: " + ae.getMessage());
            }
        }
    }
}
```

**Output:**
```
Validation [email]: Invalid format
Not found: USR-456
Permission: delete
```

**Explanation:** A custom hierarchy with `AppException` as the base allows catching at any level. Specific catches handle individual types; the base catch handles any application exception. This is the standard pattern for domain exception design.
