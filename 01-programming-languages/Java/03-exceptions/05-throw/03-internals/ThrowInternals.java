/**
 * Demonstrates JVM internals behind the throw keyword: bytecode behavior,
 * stack unwinding, and exception table lookup.
 *
 * <p>Complexity: O(n) where n is stack depth — stack unwinding traverses each frame.</p>
 *
 * <p>Thread-Safety: Yes — each demonstration method is self-contained and stateless.</p>
 *
 * <p>Key Characteristics:
 * <ul>
 *   <li>The athrow bytecode instruction pops the top-of-stack (a Throwable) and transfers control</li>
 *   <li>JVM performs stack unwinding, popping frames until a matching handler is found</li>
 *   <li>Exception table maps [start_pc, end_pc] ranges to handler_pc and caught type</li>
 *   <li>If no handler is found in the current method, the frame is popped and unwinding continues up the call stack</li>
 *   <li>finally blocks execute inline or via jsr/ret instructions depending on classfile version</li>
 * </ul>
 */
public class ThrowInternals {

    public static void main(String[] args) {
        System.out.println("=== Throw Internals ===\n");

        demonstrateAthrow();
        System.out.println();
        demonstrateStackUnwinding(0);
        System.out.println();
        demonstrateExceptionTableLookup();
    }

    /**
     * Demonstrates athrow bytecode behavior.
     *
     * <p>The throw statement compiles to the athrow JVM instruction. When executed:
     * <ol>
     *   <li>The Throwable object reference is popped from the operand stack</li>
     *   <li>The JVM checks the current method's exception table for a matching handler</li>
     *   <li>If no handler matches, the current frame is popped and the exception propagates to the caller</li>
     *   <li>The JVM walks the stack trace, filling in StackTraceElements as needed</li>
     * </ol></p>
     */
    public static void demonstrateAthrow() {
        System.out.println("--- athrow bytecode behavior ---");
        System.out.println("When 'throw new RuntimeException(\"msg\")' executes:");
        System.out.println("1. new RuntimeException — allocates object on heap, pushes ref to stack");
        System.out.println("2. ldc \"msg\" — pushes string constant onto stack");
        System.out.println("3. invokespecial RuntimeException.<init>(String) — initializes the exception");
        System.out.println("4. athrow — pops the Throwable ref and transfers control to handler");
        System.out.println();

        // This throw triggers athrow and demonstrates the handler lookup
        try {
            throw new RuntimeException("Demonstrating athrow instruction");
        } catch (RuntimeException e) {
            System.out.println("Handler matched, caught: " + e.getMessage());
            System.out.println("Stack trace at throw point:");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Demonstrates how the JVM unwinds the stack when no handler is found locally.
     *
     * <p>Each call pushes a new frame. When an exception is thrown:
     * <ul>
     *   <li>The JVM checks the exception table of the current (innermost) frame</li>
     *   <li>If no handler matches, the frame is popped (discarded)</li>
     *   <li>The exception is passed to the caller's frame for handler lookup</li>
     *   <li>This continues until a matching handler is found or the thread terminates</li>
     * </ul></p>
     *
     * @param depth current recursion depth
     */
    public static void demonstrateStackUnwinding(int depth) {
        System.out.println("--- Stack unwinding demonstration ---");
        System.out.println("Recursion depth: " + depth);

        if (depth == 3) {
            System.out.println("Throwing exception at depth " + depth + " — stack will unwind:");
            try {
                throw new RuntimeException("Unwinding from depth " + depth);
            } catch (RuntimeException e) {
                System.out.println("Caught at depth " + depth + ": " + e.getMessage());
                // Print the full stack trace to show the unwound frames
                System.out.println("Full stack trace showing frame history:");
                for (StackTraceElement ste : e.getStackTrace()) {
                    System.out.println("  at " + ste);
                }
            }
            return;
        }

        // Each recursive call creates a new stack frame
        demonstrateStackUnwinding(depth + 1);
        System.out.println("Returned to depth " + depth + " after handler");
    }

    /**
     * Demonstrates exception table lookup process.
     *
     * <p>Each compiled method contains an exception table with entries of the form:
     * <pre>
     *   [start_pc, end_pc) -> handler_pc, catch_type
     * </pre>
     * When an exception is thrown at a bytecode offset:
     * <ol>
     *   <li>The JVM searches the exception table for an entry where:
     *       <ul>
     *         <li>start_pc &le; throw_pc &lt; end_pc</li>
     *         <li>catch_type is 0 (finally) or matches the thrown type via instanceof</li>
     *       </ul>
     *   </li>
     *   <li>The first matching entry is selected (table order matters)</li>
     *   <li>Control transfers to handler_pc with the exception on the stack</li>
     *   <li>If no entry matches, the exception propagates to the caller</li>
     * </ol></p>
     */
    public static void demonstrateExceptionTableLookup() {
        System.out.println("--- Exception table lookup process ---");

        // Example: three catch blocks demonstrate lookup order
        try {
            System.out.println("Trying block that throws IllegalArgumentException");
            throw new IllegalArgumentException("Illegal arg");
        } catch (IllegalArgumentException e) {
            System.out.println("1. Matched IllegalArgumentException handler");
            System.out.println("   (JVM searched exception table, found matching type)");
        } catch (RuntimeException e) {
            // This handler is NOT reached for IllegalArgumentException
            // because the first catch already matched
            System.out.println("2. NOT REACHED — RuntimeException handler skipped");
        }

        System.out.println();

        // Demonstrates that finally (catch_type=0) always matches
        try {
            System.out.println("Trying block with finally — finally always executes");
            throw new IllegalStateException("State error");
        } catch (IllegalStateException e) {
            System.out.println("3. Matched IllegalStateException handler");
        } finally {
            System.out.println("4. Finally block executes (catch_type=0 in exception table)");
        }

        System.out.println();

        // Demonstrates subclass matching
        try {
            System.out.println("Trying block — IOException is checked, RuntimeException is unchecked");
            throw new NumberFormatException("Not a number");
        } catch (NumberFormatException e) {
            System.out.println("5. Matched NumberFormatException (most specific handler)");
        } catch (IllegalArgumentException e) {
            System.out.println("   NOT REACHED — NumberFormatException already caught above");
        }

        System.out.println();
        System.out.println("Exception table lookup order: first match wins.");
        System.out.println("Catch blocks are ordered from top to bottom in the exception table.");
    }
}
