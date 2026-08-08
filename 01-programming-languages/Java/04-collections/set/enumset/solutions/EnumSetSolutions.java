package set.enumset.solutions;

import java.util.*;

public class EnumSetSolutions {

    enum Season { SPRING, SUMMER, FALL, WINTER }

    public static <E extends Enum<E>> EnumSet<E> union(EnumSet<E> set1, EnumSet<E> set2) {
        EnumSet<E> result = EnumSet.copyOf(set1);
        result.addAll(set2);
        return result;
    }

    public static <E extends Enum<E>> boolean isSubset(EnumSet<E> subset, EnumSet<E> superset) {
        return superset.containsAll(subset);
    }

    public static <E extends Enum<E>> EnumSet<E> complement(EnumSet<E> set) {
        return EnumSet.complementOf(set);
    }

    public static EnumSet<Season> fromBooleanArray(boolean[] include) {
        Season[] seasons = Season.values();
        EnumSet<Season> result = EnumSet.noneOf(Season.class);
        for (int i = 0; i < Math.min(include.length, seasons.length); i++) {
            if (include[i]) {
                result.add(seasons[i]);
            }
        }
        return result;
    }

    public static <E extends Enum<E>> List<E> missingValues(EnumSet<E> set, Class<E> enumClass) {
        List<E> missing = new ArrayList<>();
        for (E value : enumClass.getEnumConstants()) {
            if (!set.contains(value)) {
                missing.add(value);
            }
        }
        return missing;
    }

    public static void main(String[] args) {
        EnumSet<Season> s1 = EnumSet.of(Season.SPRING, Season.SUMMER);
        EnumSet<Season> s2 = EnumSet.of(Season.SUMMER, Season.FALL);
        System.out.println("Union: " + union(s1, s2));
        System.out.println("Subset: " + isSubset(s1, EnumSet.allOf(Season.class)));
        System.out.println("Complement: " + complement(EnumSet.of(Season.SPRING)));
        System.out.println("From boolean: " + fromBooleanArray(new boolean[]{true, false, true, false}));
        System.out.println("Missing: " + missingValues(s1, Season.class));
    }
}
