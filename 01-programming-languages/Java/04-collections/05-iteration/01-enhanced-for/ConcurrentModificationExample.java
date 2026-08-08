package academy.javaengineering.collections.iteration.enhanced;

import java.util.*;

public class ConcurrentModificationExample {
    public static void main(String[] args) {
        System.out.println("=== ConcurrentModificationException Examples ===\n");

        // 1. FAIL: for-each loop + remove
        System.out.println("--- 1. for-each + remove (FAILS) ---");
        List<String> list1 = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        try {
            for (String lang : list1) {
                if (lang.equals("Python")) {
                    list1.remove(lang);  // Throws ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
            System.out.println("List unchanged: " + list1);
        }

        // 2. FAIL: for-each + add
        System.out.println("\n--- 2. for-each + add (FAILS) ---");
        List<String> list2 = new ArrayList<>(Arrays.asList("Java", "Python"));
        try {
            for (String lang : list2) {
                if (lang.equals("Java")) {
                    list2.add("C++");  // Throws ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 3. FAIL: for loop + remove (using index)
        System.out.println("\n--- 3. for loop + remove by index (PROBLEMATIC) ---");
        List<String> list3 = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        System.out.println("Before: " + list3);
        for (int i = 0; i < list3.size(); i++) {
            if (list3.get(i).equals("Python")) {
                list3.remove(i);  // Skips next element
                i--;  // Fix: decrement index
            }
        }
        System.out.println("After (with fix): " + list3);

        // 4. CORRECT: Iterator + remove
        System.out.println("\n--- 4. Iterator + remove (CORRECT) ---");
        List<String> list4 = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        Iterator<String> it = list4.iterator();
        while (it.hasNext()) {
            String lang = it.next();
            if (lang.length() > 4) {
                it.remove();  // Safe removal
            }
        }
        System.out.println("After remove: " + list4);

        // 5. CORRECT: removeIf (Java 8+)
        System.out.println("\n--- 5. removeIf (CORRECT, Java 8+) ---");
        List<String> list5 = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript"));
        list5.removeIf(s -> s.length() > 4);
        System.out.println("After removeIf: " + list5);

        // 6. CORRECT: CopyOnWriteArrayList (safe for iteration)
        System.out.println("\n--- 6. CopyOnWriteArrayList (CORRECT) ---");
        List<String> list6 = new CopyOnWriteArrayList<>(Arrays.asList("Java", "Python", "C++"));
        for (String lang : list6) {
            if (lang.equals("Python")) {
                list6.remove(lang);  // Safe with COW
            }
        }
        System.out.println("After remove: " + list6);

        // 7. FAIL: HashMap + for-each
        System.out.println("\n--- 7. HashMap for-each + remove (FAILS) ---");
        Map<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        try {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 2) {
                    map.remove(entry.getKey());  // Throws exception
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 8. CORRECT: Iterator for Map
        System.out.println("\n--- 8. Iterator for Map (CORRECT) ---");
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Java", 1);
        map2.put("Python", 2);
        map2.put("C++", 3);
        Iterator<Map.Entry<String, Integer>> mapIt = map2.entrySet().iterator();
        while (mapIt.hasNext()) {
            Map.Entry<String, Integer> entry = mapIt.next();
            if (entry.getValue() == 2) {
                mapIt.remove();  // Safe
            }
        }
        System.out.println("After remove: " + map2);

        // 9. CORRECT: ConcurrentHashMap (safe iteration)
        System.out.println("\n--- 9. ConcurrentHashMap (CORRECT) ---");
        Map<String, Integer> cmap = new ConcurrentHashMap<>();
        cmap.put("Java", 1);
        cmap.put("Python", 2);
        cmap.put("C++", 3);
        for (Map.Entry<String, Integer> entry : cmap.entrySet()) {
            if (entry.getValue() == 2) {
                cmap.remove(entry.getKey());  // Safe
            }
        }
        System.out.println("After remove: " + cmap);
    }
}
