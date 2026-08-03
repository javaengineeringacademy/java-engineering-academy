package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashSetTest {

    private HashSet<String> hashSet;

    @BeforeEach
    void setUp() {
        hashSet = new HashSet<>();
    }

    @Test
    void testCreation() {
        HashSet<String> set = new HashSet<>();
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testCreationWithCapacity() {
        HashSet<String> set = new HashSet<>(16);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testCreationWithCollection() {
        Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));
        assertEquals(3, set.size());
        assertTrue(set.contains("A"));
    }

    @Test
    void testAdd() {
        boolean added = hashSet.add("A");
        assertTrue(added);
        assertEquals(1, hashSet.size());
    }

    @Test
    void testAddDuplicate() {
        hashSet.add("A");
        boolean added = hashSet.add("A");
        assertFalse(added);
        assertEquals(1, hashSet.size());
    }

    @Test
    void testAddAll() {
        hashSet.addAll(Arrays.asList("A", "B", "C", "A"));
        assertEquals(3, hashSet.size());
        assertTrue(hashSet.contains("A"));
        assertTrue(hashSet.contains("B"));
        assertTrue(hashSet.contains("C"));
    }

    @Test
    void testContains() {
        hashSet.add("A");
        hashSet.add("B");
        assertTrue(hashSet.contains("A"));
        assertTrue(hashSet.contains("B"));
        assertFalse(hashSet.contains("C"));
    }

    @Test
    void testRemove() {
        hashSet.add("A");
        hashSet.add("B");
        boolean removed = hashSet.remove("A");
        assertTrue(removed);
        assertEquals(1, hashSet.size());
        assertFalse(hashSet.contains("A"));
    }

    @Test
    void testRemoveNonExistent() {
        hashSet.add("A");
        boolean removed = hashSet.remove("Z");
        assertFalse(removed);
        assertEquals(1, hashSet.size());
    }

    @Test
    void testSize() {
        assertEquals(0, hashSet.size());
        hashSet.add("A");
        assertEquals(1, hashSet.size());
        hashSet.add("B");
        assertEquals(2, hashSet.size());
        hashSet.add("A");
        assertEquals(2, hashSet.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(hashSet.isEmpty());
        hashSet.add("A");
        assertFalse(hashSet.isEmpty());
    }

    @Test
    void testClear() {
        hashSet.addAll(Arrays.asList("A", "B", "C"));
        hashSet.clear();
        assertEquals(0, hashSet.size());
        assertTrue(hashSet.isEmpty());
    }

    @Test
    void testIterator() {
        hashSet.addAll(Arrays.asList("A", "B", "C"));
        int count = 0;
        for (String s : hashSet) {
            assertNotNull(s);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testRemoveAll() {
        hashSet.addAll(Arrays.asList("A", "B", "C", "D"));
        hashSet.removeAll(Arrays.asList("B", "D"));
        assertEquals(2, hashSet.size());
        assertTrue(hashSet.contains("A"));
        assertTrue(hashSet.contains("C"));
        assertFalse(hashSet.contains("B"));
        assertFalse(hashSet.contains("D"));
    }

    @Test
    void testRetainAll() {
        hashSet.addAll(Arrays.asList("A", "B", "C", "D"));
        hashSet.retainAll(Arrays.asList("B", "C", "E"));
        assertEquals(2, hashSet.size());
        assertTrue(hashSet.contains("B"));
        assertTrue(hashSet.contains("C"));
        assertFalse(hashSet.contains("A"));
    }

    @Test
    void testToArray() {
        hashSet.addAll(Arrays.asList("A", "B"));
        Object[] array = hashSet.toArray();
        assertEquals(2, array.length);
    }

    @Test
    void testToArrayWithGenericArray() {
        hashSet.addAll(Arrays.asList("A", "B"));
        String[] array = hashSet.toArray(new String[0]);
        assertEquals(2, array.length);
        assertEquals("A", array[0]);
    }

    @Test
    void testAddNull() {
        hashSet.add(null);
        assertEquals(1, hashSet.size());
        assertTrue(hashSet.contains(null));
    }

    @Test
    void testAddMultipleNulls() {
        hashSet.add(null);
        hashSet.add(null);
        assertEquals(1, hashSet.size());
    }

    @Test
    void testRemoveNull() {
        hashSet.add(null);
        hashSet.add("A");
        boolean removed = hashSet.remove(null);
        assertTrue(removed);
        assertEquals(1, hashSet.size());
        assertFalse(hashSet.contains(null));
    }

    @Test
    void testEquals() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<String> set2 = new HashSet<>(Arrays.asList("A", "B"));
        assertEquals(set1, set2);
    }

    @Test
    void testHashCode() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<String> set2 = new HashSet<>(Arrays.asList("A", "B"));
        assertEquals(set1.hashCode(), set2.hashCode());
    }

    @Test
    void testToString() {
        hashSet.add("A");
        assertNotNull(hashSet.toString());
    }

    @Test
    void testClone() {
        hashSet.addAll(Arrays.asList("A", "B"));
        @SuppressWarnings("unchecked")
        HashSet<String> cloned = (HashSet<String>) hashSet.clone();
        assertEquals(hashSet, cloned);
        cloned.add("C");
        assertFalse(hashSet.contains("C"));
    }

    @Test
    void testUnion() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));
        set1.addAll(set2);
        assertEquals(4, set1.size());
        assertEquals(Set.of("A", "B", "C", "D"), set1);
    }

    @Test
    void testIntersection() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));
        set1.retainAll(set2);
        assertEquals(2, set1.size());
        assertEquals(Set.of("B", "C"), set1);
    }

    @Test
    void testDifference() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));
        set1.removeAll(set2);
        assertEquals(1, set1.size());
        assertEquals(Set.of("A"), set1);
    }

    @Test
    void testContainsAll() {
        hashSet.addAll(Arrays.asList("A", "B", "C"));
        assertTrue(hashSet.containsAll(Arrays.asList("A", "B")));
        assertFalse(hashSet.containsAll(Arrays.asList("A", "D")));
    }

    @Test
    void testLargeSet() {
        for (int i = 0; i < 1000; i++) {
            hashSet.add("item" + i);
        }
        assertEquals(1000, hashSet.size());
        assertTrue(hashSet.contains("item0"));
        assertTrue(hashSet.contains("item999"));
    }
}
