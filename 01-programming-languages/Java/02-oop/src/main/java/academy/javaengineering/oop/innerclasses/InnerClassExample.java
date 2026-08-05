package academy.javaengineering.oop.innerclasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates member, local, anonymous, and static nested inner classes.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Member inner classes: non-static, have access to enclosing instance</li>
 *   <li>Static nested classes: static, no enclosing instance needed</li>
 *   <li>Local inner classes: defined inside a method</li>
 *   <li>Anonymous inner classes: inline class definitions</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class InnerClassExample {

    // ==================== Static Nested Classes ====================

    /**
     * Static nested class - independent of outer instance.
     * Used when the nested class doesn't need access to outer instance fields.
     */
    public static class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public T getData() { return data; }
        public Node<T> getNext() { return next; }
        public void setNext(Node<T> next) { this.next = next; }

        @Override
        public String toString() {
            return "Node{" + data + "}";
        }
    }

    // ==================== Member Inner Classes ====================

    /**
     * Linked list using member inner class (has access to outer instance).
     */
    public static class LinkedList<T> {
        private Node<T> head;
        private int size;

        /**
         * Member inner class - iterator has implicit access to LinkedList's fields.
         */
        public class ListIterator {
            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                if (current == null) throw new java.util.NoSuchElementException();
                T data = current.getData();
                current = current.getNext();
                return data;
            }

            /** Has access to outer class's size field directly. */
            public int remaining() {
                int count = 0;
                Node<T> node = current;
                while (node != null) {
                    count++;
                    node = node.getNext();
                }
                return count;
            }
        }

        public void add(T data) {
            Node<T> newNode = new Node<>(data);
            newNode.setNext(head);
            head = newNode;
            size++;
        }

        public int getSize() { return size; }

        /** Factory method returning member inner class instance. */
        public ListIterator iterator() {
            return new ListIterator();
        }

        /**
         * Returns a filtered list using member inner class.
         */
        public List<T> toList() {
            List<T> result = new ArrayList<>();
            ListIterator iter = iterator();
            while (iter.hasNext()) {
                result.add(iter.next());
            }
            return result;
        }
    }

    // ==================== Local Inner Classes ====================

    /**
     * Demonstrates local inner classes defined inside methods.
     */
    public static class Validator {
        private final List<String> errors = new ArrayList<>();

        /**
         * Local inner class defined inside a method.
         */
        public List<String> validate(String input) {
            class InputRule {
                private final String fieldName;
                private final int minLength;
                private final int maxLength;

                InputRule(String fieldName, int minLength, int maxLength) {
                    this.fieldName = fieldName;
                    this.minLength = minLength;
                    this.maxLength = maxLength;
                }

                boolean isValid(String value) {
                    return value != null
                            && value.length() >= minLength
                            && value.length() <= maxLength;
                }

                String getErrorMessage() {
                    return fieldName + " must be between " + minLength + " and " + maxLength + " characters";
                }
            }

            List<String> validationErrors = new ArrayList<>();

            InputRule usernameRule = new InputRule("Username", 3, 20);
            InputRule emailRule = new InputRule("Email", 5, 100);

            if (!usernameRule.isValid(input)) {
                validationErrors.add(usernameRule.getErrorMessage());
            }
            if (!emailRule.isValid(input)) {
                validationErrors.add(emailRule.getErrorMessage());
            }

            return validationErrors;
        }
    }

    // ==================== Anonymous Inner Classes ====================

    /**
     * Demonstrates anonymous inner classes for inline implementations.
     */
    public static class EventManager {
        public interface EventHandler {
            void handle(String event);
            String getName();
        }

        private final List<EventHandler> handlers = new ArrayList<>();

        public void register(EventHandler handler) {
            handlers.add(handler);
        }

        public void fireEvent(String event) {
            for (EventHandler handler : handlers) {
                handler.handle(event);
            }
        }
    }

    /**
     * Task scheduler using anonymous inner classes.
     */
    public static class TaskScheduler {
        public interface Task {
            void execute();
            String getTaskName();
        }

        private final List<Task> tasks = new ArrayList<>();

        public void schedule(Task task) {
            tasks.add(task);
        }

        public void runAll() {
            for (Task task : tasks) {
                System.out.println("  Running: " + task.getTaskName());
                task.execute();
            }
        }

        public int getTaskCount() { return tasks.size(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Inner Classes Demo ===\n");

        // Static nested class
        System.out.println("--- Static Nested Class ---");
        Node<String> node1 = new Node<>("first");
        Node<String> node2 = new Node<>("second");
        node1.setNext(node2);
        System.out.println("Node: " + node1 + " -> " + node1.getNext());

        // Member inner class
        System.out.println("\n--- Member Inner Class (LinkedList) ---");
        LinkedList<String> list = new LinkedList<>();
        list.add("Charlie");
        list.add("Bravo");
        list.add("Alpha");

        System.out.println("Size: " + list.getSize());
        LinkedList.ListIterator iter = list.iterator();
        System.out.print("Elements: ");
        while (iter.hasNext()) {
            System.out.print(iter.next());
            if (iter.remaining() > 0) System.out.print(" -> ");
        }
        System.out.println();

        // Local inner class
        System.out.println("\n--- Local Inner Class ---");
        Validator validator = new Validator();
        System.out.println("Validate 'ab':     " + validator.validate("ab"));
        System.out.println("Validate 'alice':  " + validator.validate("alice"));
        System.out.println("Validate 'a':      " + validator.validate("a"));

        // Anonymous inner classes
        System.out.println("\n--- Anonymous Inner Classes ---");
        EventManager eventManager = new EventManager();

        // Anonymous implementation of EventHandler interface
        eventManager.register(new EventManager.EventHandler() {
            @Override
            public void handle(String event) {
                System.out.println("  [Logger] Event: " + event);
            }

            @Override
            public String getName() { return "Logger"; }
        });

        eventManager.register(new EventManager.EventHandler() {
            @Override
            public void handle(String event) {
                System.out.println("  [Notifier] Alert: " + event.toUpperCase());
            }

            @Override
            public String getName() { return "Notifier"; }
        });

        eventManager.fireEvent("user_login");

        // Anonymous class for Task
        System.out.println("\n--- Anonymous Task Classes ---");
        TaskScheduler scheduler = new TaskScheduler();

        scheduler.schedule(new TaskScheduler.Task() {
            @Override
            public void execute() {
                System.out.println("    Database backup complete");
            }

            @Override
            public String getTaskName() { return "BackupTask"; }
        });

        scheduler.schedule(new TaskScheduler.Task() {
            @Override
            public void execute() {
                System.out.println("    Cache cleared");
            }

            @Override
            public String getTaskName() { return "CacheClearTask"; }
        });

        System.out.println("Scheduled " + scheduler.getTaskCount() + " tasks:");
        scheduler.runAll();
    }
}
