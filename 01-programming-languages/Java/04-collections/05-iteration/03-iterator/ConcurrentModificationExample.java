package academy.javaengineering.collections.iteration.iterator;

import java.util.*;

public class ConcurrentModificationExample {
    public static void main(String[] args) {
        System.out.println("=== Iterator ConcurrentModificationException ===\n");

        // 1. FAIL: for-each + remove
        System.out.println("--- 1. for-each + remove (FAILS) ---");
        List<String> list1 = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        try {
            for (String lang : list1) {
                if (lang.equals("Python")) {
                    list1.remove(lang);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 2. CORRECT: Iterator + remove
        System.out.println("\n--- 2. Iterator + remove (CORRECT) ---");
        List<String> list2 = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        Iterator<String> it = list2.iterator();
        while (it.hasNext()) {
            String lang = it.next();
            if (lang.equals("Python")) {
                it.remove();
            }
        }
        System.out.println("After remove: " + list2);

        // 3. FAIL: Iterator + list.remove() instead of it.remove()
        System.out.println("\n--- 3. list.remove() inside Iterator (FAILS) ---");
        List<String> list3 = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        try {
            Iterator<String> it3 = list3.iterator();
            while (it3.hasNext()) {
                it3.next();
                list3.remove(0);  // Wrong! Should use it.remove()
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 4. FAIL: Iterator + add
        System.out.println("\n--- 4. list.add() inside Iterator (FAILS) ---");
        List<String> list4 = new ArrayList<>(Arrays.asList("Java", "C++"));
        try {
            Iterator<String> it4 = list4.iterator();
            while (it4.hasNext()) {
                String lang = it4.next();
                if (lang.equals("Java")) {
                    list4.add("Python");  // Wrong! Use Iterator.add()
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 5. CORRECT: Iterator + add
        System.out.println("\n--- 5. Iterator.add() (CORRECT) ---");
        List<String> list5 = new ArrayList<>(Arrays.asList("Java", "C++"));
        Iterator<String> it5 = list5.iterator();
        while (it5.hasNext()) {
            String lang = it5.next();
            if (lang.equals("Java")) {
                it5.add("Python");  // Correct!
            }
        }
        System.out.println("After add: " + list5);

        // 6. CORRECT: ListIterator (can add and set)
        System.out.println("\n--- 6. ListIterator (CORRECT) ---");
        List<String> list6 = new ArrayList<>(Arrays.asList("Java", "C++"));
        ListIterator<String> lit = list6.listIterator();
        while (lit.hasNext()) {
            String lang = lit.next();
            if (lang.equals("Java")) {
                lit.add("Python");
                lit.set("Java8");
            }
        }
        System.out.println("After modify: " + list6);

        // 7. CORRECT: CopyOnWriteArrayList
        System.out.println("\n--- 7. CopyOnWriteArrayList (CORRECT) ---");
        List<String> list7 = new CopyOnWriteArrayList<>(Arrays.asList("Java", "Python", "C++"));
        for (String lang : list7) {
            if (lang.equals("Python")) {
                list7.remove(lang);
            }
        }
        System.out.println("After remove: " + list7);

        // 8. FAIL: HashMap Iterator + remove key
        System.out.println("\n--- 8. HashMap Iterator + removeKey (FAILS) ---");
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("Java", 1);
        map1.put("Python", 2);
        map1.put("C++", 3);
        try {
            Iterator<Map.Entry<String, Integer>> mapIt = map1.entrySet().iterator();
            while (mapIt.hasNext()) {
                Map.Entry<String, Integer> entry = mapIt.next();
                if (entry.getValue() == 2) {
                    map1.remove(entry.getKey());
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }

        // 9. CORRECT: HashMap Iterator + remove
        System.out.println("\n--- 9. HashMap Iterator.remove() (CORRECT) ---");
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Java", 1);
        map2.put("Python", 2);
        map2.put("C++", 3);
        Iterator<Map.Entry<String, Integer>> mapIt2 = map2.entrySet().iterator();
        while (mapIt2.hasNext()) {
            Map.Entry<String, Integer> entry = mapIt2.next();
            if (entry.getValue() == 2) {
                mapIt2.remove();
            }
        }
        System.out.println("After remove: " + map2);

        // 10. CORRECT: ConcurrentHashMap
        System.out.println("\n--- 10. ConcurrentHashMap (CORRECT) ---");
        Map<String, Integer> cmap = new ConcurrentHashMap<>();
        cmap.put("Java", 1);
        cmap.put("Python", 2);
        cmap.put("C++", 3);
        for (Map.Entry<String, Integer> entry : cmap.entrySet()) {
            if (entry.getValue() == 2) {
                cmap.remove(entry.getKey());
            }
        }
        System.out.println("After remove: " + cmap);
    }
}
