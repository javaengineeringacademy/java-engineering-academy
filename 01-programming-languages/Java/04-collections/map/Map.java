package academy.javaengineering.collections.map;

import java.util.Set;
import java.util.Collection;

/**
 * Map - Key-value pair mapping (no duplicate keys).
 *
 * Internal: Backed by hash table (HashMap), red-black tree (TreeMap),
 *           or hash+linked list (LinkedHashMap)
 * Hierarchy: Map<K,V> (standalone, does NOT extend Collection)
 *
 * Complexity: put O(1) hash / O(log n) tree, get O(1) / O(log n), remove O(1) / O(log n)
 * Thread-safe: NO
 *
 * Key characteristics:
 *   - Key-value mapping: Each key maps to at most one value
 *   - No duplicate keys: put() replaces existing value for key
 *   - Keys must implement hashCode() and equals() properly
 *   - At most one null key (HashMap), no null keys (Hashtable/ConcurrentHashMap)
 *   - Multiple null values allowed (HashMap)
 *
 * Common implementations: HashMap, TreeMap, LinkedHashMap, Hashtable, ConcurrentHashMap
 */
public interface Map<K, V> {

    /** Associates key with value. Returns previous value or null. Time: O(1) hash */
    V put(K key, V value);

    /** Returns value for key, or null if not present. Time: O(1) hash, O(log n) tree */
    V get(Object key);

    /** Returns true if map contains key. Time: O(1) hash */
    boolean containsKey(Object key);

    /** Returns true if map contains value. Time: O(n) must scan values */
    boolean containsValue(Object value);

    /** Removes mapping for key. Returns previous value or null. Time: O(1) */
    V remove(Object key);

    /** Returns number of key-value mappings. Time: O(1) */
    int size();

    /** Returns true if empty. Time: O(1) */
    boolean isEmpty();

    /** Removes all mappings. Time: O(n) */
    void clear();

    /** Returns set of all keys. Time: O(1) (view) */
    Set<K> keySet();

    /** Returns collection of all values. Time: O(1) (view) */
    Collection<V> values();

    /** Returns set of all key-value mappings. Time: O(1) (view) */
    Set<Map.Entry<K, V>> entrySet();

    /** Returns value for key, inserting default value if absent. Time: O(1) */
    V getOrDefault(Object key, V defaultValue);

    /** If key absent, computes value with lambda and puts it. Time: O(1) */
    V putIfAbsent(K key, java.util.function.Function<K, V> mappingFunction);

    /** Applies action to each entry. Time: O(n) */
    void forEach(java.util.function.BiConsumer<? super K, ? super V> action);

    /**
     * Entry - A key-value pair stored in Map.
     */
    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V value);
    }
}
