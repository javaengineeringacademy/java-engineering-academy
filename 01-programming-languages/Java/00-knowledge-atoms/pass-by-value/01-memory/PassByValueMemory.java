package academy.javaengineering.knowledgeatoms.passbyvalue;

import java.util.ArrayList;
import java.util.List;

public class PassByValueMemory {

    public static void main(String[] args) {
        System.out.println("=== Pass by Value Memory Analysis ===\n");

        // 1. Stack frame anatomy
        System.out.println("--- Stack Frame Anatomy ---");
        System.out.println("Each method call creates a stack frame containing:");
        System.out.println("  - Local variables");
        System.out.println("  - Copied method parameters");
        System.out.println("  - Return address");
        System.out.println("  - Operand stack");
        System.out.println("Stack frame size: ~16-64 bytes (varies by method)");

        // 2. Reference copying
        System.out.println("\n--- Reference Copying ---");
        demonstrateReferenceCopying();

        // 3. Object sharing via copied references
        System.out.println("\n--- Object Sharing ---");
        demonstrateObjectSharing();

        // 4. Return value memory
        System.out.println("\n--- Return Value Memory ---");
        demonstrateReturnValues();

        // 5. Memory implications
        System.out.println("\n--- Memory Implications ---");
        System.out.println("Pass-by-value means:");
        System.out.println("  - No risk of accidental reference corruption");
        System.out.println("  - Each method call has its own copy of parameters");
        System.out.println("  - Objects are shared via copied references (not copied)");
        System.out.println("  - Returning objects creates a new reference on the caller's stack");
    }

    private static void demonstrateReferenceCopying() {
        StringBuilder original = new StringBuilder("Hello");
        StringBuilder copy = original;  // copy of reference

        System.out.println("original and copy point to same object:");
        System.out.println("  original: " + System.identityHashCode(original));
        System.out.println("  copy:     " + System.identityHashCode(copy));
        System.out.println("  Same identity hash code: " + (System.identityHashCode(original) == System.identityHashCode(copy)));
    }

    private static void demonstrateObjectSharing() {
        List<String> list = new ArrayList<>();
        list.add("Hello");

        addTo(list);  // reference is copied, object is shared
        System.out.println("After addTo(): list = " + list);

        replaceList(list);  // reference is copied, reassignment is local
        System.out.println("After replaceList(): list = " + list);
    }

    private static void addTo(List<String> list) {
        list.add("World");  // modifies shared object
    }

    private static void replaceList(List<String> list) {
        list = new ArrayList<>();  // reassigns local copy
        list.add("Goodbye");
    }

    private static void demonstrateReturnValues() {
        StringBuilder sb = createObject();
        System.out.println("Returned object reference: " + System.identityHashCode(sb));

        StringBuilder modified = modifyAndReturn(sb);
        System.out.println("Modified object reference: " + System.identityHashCode(modified));
        System.out.println("Same object: " + (sb == modified));
    }

    private static StringBuilder createObject() {
        return new StringBuilder("Created");
    }

    private static StringBuilder modifyAndReturn(StringBuilder sb) {
        sb.append(" and Modified");
        return sb;  // returns the same reference
    }
}
