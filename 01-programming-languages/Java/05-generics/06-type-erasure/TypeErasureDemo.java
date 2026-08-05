package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;

public class TypeErasureDemo {

    public static <T> void demonstrateTypeErasure() {
        List<String> stringList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        System.out.println("Same runtime class: " + (stringList.getClass() == intList.getClass()));
    }

    public static void demonstrateRawTypes() {
        List<String> typedList = new ArrayList<>();
        typedList.add("Hello");
        List rawList = typedList;
        System.out.println("Same object: " + (typedList == rawList));
    }

    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) max = list.get(i);
        }
        return max;
    }

    interface Container<T> {
        void set(T value);
        T get();
    }

    static class StringContainer implements Container<String> {
        private String value;

        @Override
        public void set(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }
    }
}
