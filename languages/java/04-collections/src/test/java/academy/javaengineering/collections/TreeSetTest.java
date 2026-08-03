package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TreeSetTest {

    private TreeSet<String> treeSet;

    @BeforeEach
    void setUp() {
        treeSet = new TreeSet<>();
    }

    @Test
    void testCreation() {
        TreeSet<String> set = new TreeSet<>();
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testCreationWithComparator() {
        TreeSet<String> set = new TreeSet<>(Comparator.reverseOrder());
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testCreationWithSortedElements() {
        TreeSet<String> set = new TreeSet<>(Arrays.asList("C", "A", "B"));
        assertEquals(3, set.size());
        assertEquals("A", set.first());
        assertEquals("C", set.last());
    }

    @Test
    void testAdd() {
        boolean added = treeSet.add("B");
        assertTrue(added);
        treeSet.add("A");
        treeSet.add("C");
        assertEquals(3, treeSet.size());
        assertEquals("A", treeSet.first());
        assertEquals("C", treeSet.last());
    }

    @Test
    void testAddDuplicate() {
        treeSet.add("A");
        boolean added = treeSet.add("A");
        assertFalse(added);
        assertEquals(1, treeSet.size());
    }

    @Test
    void testAddAll() {
        treeSet.addAll(Arrays.asList("C", "A", "B", "A"));
        assertEquals(3, treeSet.size());
        assertEquals("A", treeSet.first());
        assertEquals("C", treeSet.last());
    }

    @Test
    void testContains() {
        treeSet.add("A");
        treeSet.add("B");
        assertTrue(treeSet.contains("A"));
        assertTrue(treeSet.contains("B"));
        assertFalse(treeSet.contains("C"));
    }

    @Test
    void testRemove() {
        treeSet.add("A");
        treeSet.add("B");
        treeSet.add("C");
        boolean removed = treeSet.remove("B");
        assertTrue(removed);
        assertEquals(2, treeSet.size());
        assertFalse(treeSet.contains("B"));
    }

    @Test
    void testRemoveNonExistent() {
        treeSet.add("A");
        boolean removed = treeSet.remove("Z");
        assertFalse(removed);
    }

    @Test
    void testSize() {
        assertEquals(0, treeSet.size());
        treeSet.add("A");
        assertEquals(1, treeSet.size());
        treeSet.add("B");
        assertEquals(2, treeSet.size());
        treeSet.add("A");
        assertEquals(2, treeSet.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(treeSet.isEmpty());
        treeSet.add("A");
        assertFalse(treeSet.isEmpty());
    }

    @Test
    void testClear() {
        treeSet.addAll(Arrays.asList("A", "B", "C"));
        treeSet.clear();
        assertEquals(0, treeSet.size());
        assertTrue(treeSet.isEmpty());
    }

    @Test
    void testFirst() {
        treeSet.add("C");
        treeSet.add("A");
        treeSet.add("B");
        assertEquals("A", treeSet.first());
    }

    @Test
    void testLast() {
        treeSet.add("C");
        treeSet.add("A");
        treeSet.add("B");
        assertEquals("C", treeSet.last());
    }

    @Test
    void testFirstOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> treeSet.first());
    }

    @Test
    void testLastOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> treeSet.last());
    }

    @Test
    void testPollFirst() {
        treeSet.add("C");
        treeSet.add("A");
        String polled = treeSet.pollFirst();
        assertEquals("A", polled);
        assertEquals(2, treeSet.size());
    }

    @Test
    void testPollLast() {
        treeSet.add("C");
        treeSet.add("A");
        String polled = treeSet.pollLast();
        assertEquals("C", polled);
        assertEquals(1, treeSet.size());
    }

    @Test
    void testPollFirstEmpty() {
        assertEquals(null, treeSet.pollFirst());
    }

    @Test
    void testPollLastEmpty() {
        assertEquals(null, treeSet.pollLast());
    }

    @Test
    void testHigher() {
        treeSet.addAll(Arrays.asList("A", "C", "E"));
        assertEquals("C", treeSet.higher("A"));
        assertEquals("E", treeSet.higher("C"));
        assertEquals(null, treeSet.higher("E"));
    }

    @Test
    void testLower() {
        treeSet.addAll(Arrays.asList("A", "C", "E"));
        assertEquals("A", treeSet.lower("C"));
        assertEquals("C", treeSet.lower("E"));
        assertEquals(null, treeSet.lower("A"));
    }

    @Test
    void testCeiling() {
        treeSet.addAll(Arrays.asList("A", "C", "E"));
        assertEquals("C", treeSet.ceiling("B"));
        assertEquals("A", treeSet.ceiling("A"));
    }

    @Test
    void testFloor() {
        treeSet.addAll(Arrays.asList("A", "C", "E"));
        assertEquals("A", treeSet.floor("B"));
        assertEquals("C", treeSet.floor("C"));
    }

    @Test
    void testHeadSet() {
        treeSet.addAll(Arrays.asList("A", "B", "C", "D"));
        Set<String> head = treeSet.headSet("C");
        assertEquals(2, head.size());
        assertTrue(head.contains("A"));
        assertTrue(head.contains("B"));
        assertFalse(head.contains("C"));
    }

    @Test
    void testTailSet() {
        treeSet.addAll(Arrays.asList("A", "B", "C", "D"));
        Set<String> tail = treeSet.tailSet("B");
        assertEquals(3, tail.size());
        assertTrue(tail.contains("B"));
        assertTrue(tail.contains("C"));
        assertTrue(tail.contains("D"));
        assertFalse(tail.contains("A"));
    }

    @Test
    void testSubSet() {
        treeSet.addAll(Arrays.asList("A", "B", "C", "D"));
        Set<String> sub = treeSet.subSet("B", "D");
        assertEquals(2, sub.size());
        assertTrue(sub.contains("B"));
        assertTrue(sub.contains("C"));
        assertFalse(sub.contains("D"));
    }

    @Test
    void testDescendingSet() {
        treeSet.addAll(Arrays.asList("A", "B", "C"));
        TreeSet<String> desc = (TreeSet<String>) treeSet.descendingSet();
        assertEquals("C", desc.first());
        assertEquals("A", desc.last());
    }

    @Test
    void testIterator() {
        treeSet.addAll(Arrays.asList("C", "A", "B"));
        String prev = "";
        for (String s : treeSet) {
            assertTrue(s.compareTo(prev) >= 0);
            prev = s;
        }
    }

    @Test
    void testRemoveAll() {
        treeSet.addAll(Arrays.asList("A", "B", "C", "D"));
        treeSet.removeAll(Arrays.asList("B", "D"));
        assertEquals(2, treeSet.size());
        assertTrue(treeSet.contains("A"));
        assertTrue(treeSet.contains("C"));
    }

    @Test
    void testRetainAll() {
        treeSet.addAll(Arrays.asList("A", "B", "C", "D"));
        treeSet.retainAll(Arrays.asList("B", "C", "E"));
        assertEquals(2, treeSet.size());
        assertTrue(treeSet.contains("B"));
        assertTrue(treeSet.contains("C"));
    }

    @Test
    void testToArray() {
        treeSet.addAll(Arrays.asList("A", "B"));
        Object[] array = treeSet.toArray();
        assertEquals(2, array.length);
    }

    @Test
    void testToArrayWithArray() {
        treeSet.addAll(Arrays.asList("A", "B"));
        String[] array = treeSet.toArray(new String[0]);
        assertEquals(2, array.length);
        assertEquals("A", array[0]);
    }

    @Test
    void testContainsAll() {
        treeSet.addAll(Arrays.asList("A", "B", "C"));
        assertTrue(treeSet.containsAll(Arrays.asList("A", "B")));
        assertFalse(treeSet.containsAll(Arrays.asList("A", "D")));
    }

    @Test
    void testEquals() {
        Set<String> set1 = new TreeSet<>(Arrays.asList("A", "B"));
        Set<String> set2 = new TreeSet<>(Arrays.asList("A", "B"));
        assertEquals(set1, set2);
    }

    @Test
    void testToString() {
        treeSet.add("A");
        treeSet.add("B");
        assertEquals("[A, B]", treeSet.toString());
    }

    @Test
    void testClone() {
        treeSet.addAll(Arrays.asList("A", "B"));
        @SuppressWarnings("unchecked")
        TreeSet<String> cloned = (TreeSet<String>) treeSet.clone();
        assertEquals(treeSet, cloned);
        cloned.add("C");
        assertFalse(treeSet.contains("C"));
    }

    @Test
    void testNaturalOrdering() {
        treeSet.add("Z");
        treeSet.add("A");
        treeSet.add("M");
        Object[] elements = treeSet.toArray();
        assertEquals("A", elements[0]);
        assertEquals("M", elements[1]);
        assertEquals("Z", elements[2]);
    }

    @Test
    void testReverseOrdering() {
        TreeSet<String> reverseSet = new TreeSet<>(Comparator.reverseOrder());
        reverseSet.add("Z");
        reverseSet.add("A");
        reverseSet.add("M");
        Object[] elements = reverseSet.toArray();
        assertEquals("Z", elements[0]);
        assertEquals("M", elements[1]);
        assertEquals("A", elements[2]);
    }

    @Test
    void testAddNullThrowsException() {
        assertThrows(NullPointerException.class, () -> treeSet.add(null));
    }

    @Test
    void testLargeTreeSet() {
        for (int i = 0; i < 1000; i++) {
            treeSet.add("item" + i);
        }
        assertEquals(1000, treeSet.size());
        assertEquals("item0", treeSet.first());
        assertEquals("item999", treeSet.last());
    }
}
