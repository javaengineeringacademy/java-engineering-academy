# Internals: Custom Annotations

## How Annotations Are Stored

### Bytecode Representation

Annotations are stored in the class file as:
- **RuntimeVisibleAnnotations** — Available via reflection (RUNTIME retention)
- **RuntimeInvisibleAnnotations** — Not available at runtime (CLASS retention)
- **AnnotationDefault** — Default values for annotation elements

### Annotation Object Creation

When you call `clazz.getAnnotation(MyAnnotation.class)`:
1. JVM checks if the annotation has RUNTIME retention
2. Looks up the annotation data in the class metadata
3. Creates a proxy instance of the annotation type
4. Returns the proxy with element values

### Annotation Element Types

Annotation elements are limited to:
- Primitives (int, long, double, etc.)
- String
- Class or Class parameterized
- Enum types
- Annotation types
- Arrays of the above

No constructors, no instance fields, no generic type parameters.

### Meta-Annotations

Meta-annotations are annotations applied to other annotations:
- @Retention — Controls when annotation is available
- @Target — Controls where annotation can be applied
- @Inherited — Subclasses inherit class-level annotations
- @Repeatable — Annotation can appear multiple times
- @Documented — Include in Javadoc

### @Inherited Implementation

@Inherited only works with class-level annotations. When checking for an annotation:
1. Check the class itself
2. If not found, check the superclass
3. Continue up the hierarchy
4. Stops at the first match (does not check interfaces)
