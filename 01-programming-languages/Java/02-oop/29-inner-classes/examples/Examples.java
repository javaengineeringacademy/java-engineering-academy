package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Inner Class Patterns ===\n");

        // WHY: Inner classes access enclosing instance's private members
        // INTERNAL: Compiler generates synthetic access methods, outer reference stored
        // ENGINEERING: Use for helper classes tightly coupled to one class

        LinkedList list = new LinkedList();
        list.add(10);
        list.add(20);
        list.add(30);

        // Iterator is a natural inner class
        LinkedList.Iterator it = list.iterator();
        while (it.hasNext()) {
            System.out.println("  " + it.next());
        }

        // TRADE-OFF: Inner class vs top-level class
        // Inner class: access to outer state, hidden from outside
        // Top-level: independent, reusable, no coupling
    }
}

class LinkedList {
    private Node head;

    private class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    public void add(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    public Iterator iterator() { return new Iterator(); }

    public class Iterator {
        private Node current = head;

        public boolean hasNext() { return current != null; }
        public int next() {
            int data = current.data;
            current = current.next;
            return data;
        }
    }
}
