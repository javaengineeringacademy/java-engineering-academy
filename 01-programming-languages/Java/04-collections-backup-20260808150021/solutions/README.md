# Solutions

## Overview

Solutions to all exercises in the Collections module. Attempt the exercises before viewing solutions.

## List Exercises Solutions

### 1. Remove Duplicates Preserving Order
```java
public static <T> List<T> removeDuplicates(List<T> list) {
    Set<T> seen = new LinkedHashSet<>(list);
    return new ArrayList<>(seen);
}
```

### 2. Rotate List
```java
public static <T> void rotate(List<T> list, int positions) {
    int size = list.size();
    if (size == 0) return;
    positions = positions % size;
    if (positions < 0) positions += size;
    Collections.rotate(list, positions);
}
```

### 3. Chunk List
```java
public static <T> List<List<T>> chunk(List<T> list, int size) {
    List<List<T>> chunks = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
        chunks.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
    }
    return chunks;
}
```

## Map Exercises Solutions

### 1. Word Frequency Counter
```java
public static Map<String, Integer> countWords(String text) {
    Map<String, Integer> wordCount = new HashMap<>();
    for (String word : text.toLowerCase().split("\\W+")) {
        if (!word.isEmpty()) {
            wordCount.merge(word, 1, Integer::sum);
        }
    }
    return wordCount;
}
```

### 2. Sort Map by Values
```java
public static <K, V extends Comparable<V>> Map<K, V> sortByValues(Map<K, V> map) {
    return map.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (a, b) -> a,
            LinkedHashMap::new
        ));
}
```

## Sorting Exercises Solutions

### 1. Sort by Length, Then Alphabetically
```java
Comparator<String> byLengthThenAlpha = Comparator
    .comparingInt(String::length)
    .thenComparing(Comparator.naturalOrder());
list.sort(byLengthThenAlpha);
```
