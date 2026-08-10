# Summary of 06-sorting Restructuring

## Overview
All 4 subfolders in `06-sorting/` have been restructured to match the queue pattern.

## Files Moved

### 00-comparable/
- `ComparableExample.java` → `00-examples/ComparableExample.java`
- `ComparableTest.java` → `00-examples/ComparableTest.java`

### 01-comparator/
- `ComparatorExample.java` → `00-examples/ComparatorExample.java`
- `ComparatorTest.java` → `00-examples/ComparatorTest.java`

### 02-collections-sort/
- `CollectionsSortExample.java` → `00-examples/CollectionsSortExample.java`
- `CollectionsSortTest.java` → `00-examples/CollectionsSortTest.java`

### 03-tim-sort/
- `TimSortExample.java` → `00-examples/TimSortExample.java`
- `TimSortTest.java` → `00-examples/TimSortTest.java`

## Files Created

### 00-comparable/
- `01-exercises/ComparableExercises.java` - 5 TODO exercises
- `02-solutions/ComparableSolutions.java` - Solutions to exercises
- `03-internals/README.md` - Internal algorithm details
- `04-memory/README.md` - Memory behavior information

### 01-comparator/
- `01-exercises/ComparatorExercises.java` - 5 TODO exercises
- `02-solutions/ComparatorSolutions.java` - Solutions to exercises
- `03-internals/README.md` - Internal algorithm details
- `04-memory/README.md` - Memory behavior information

### 02-collections-sort/
- `01-exercises/CollectionsSortExercises.java` - 5 TODO exercises
- `02-solutions/CollectionsSortSolutions.java` - Solutions to exercises
- `03-internals/README.md` - Internal algorithm details
- `04-memory/README.md` - Memory behavior information

### 03-tim-sort/
- `01-exercises/TimSortExercises.java` - 5 TODO exercises
- `02-solutions/TimSortSolutions.java` - Solutions to exercises
- `03-internals/README.md` - Internal algorithm details
- `04-memory/README.md` - Memory behavior information

## Files Kept at Root Level (unchanged)
- `README.md`
- `decision.md`
- `quiz.md`
- `references.md`

## Total Files
- **Moved**: 8 files (2 per subfolder)
- **Created**: 16 files (4 per subfolder)
- **Total**: 24 files affected

## Structure Verification
All subfolders now follow the queue pattern:
```
00-comparable/
├── 00-examples/
│   ├── ComparableExample.java
│   └── ComparableTest.java
├── 01-exercises/
│   └── ComparableExercises.java
├── 02-solutions/
│   └── ComparableSolutions.java
├── 03-internals/
│   └── README.md
├── 04-memory/
│   └── README.md
├── README.md
├── decision.md
├── quiz.md
└── references.md
```