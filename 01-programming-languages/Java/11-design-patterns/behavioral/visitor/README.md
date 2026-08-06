# Visitor Pattern

## Overview
The Visitor pattern represents an operation to be performed on elements of an object structure. It lets you define a new operation without changing the classes of the elements on which it operates.

## When to Use
- Many unrelated operations on object structures
- Class definitions rarely change but operations frequently change
- Need to group related operations without placing them in the class
- Compiler AST traversal, document processing

## Code Structure
```
Visitor (interface)         Element (interface)
    |                           |
ConcreteVisitor          accept(Visitor)
    |                           |
visit(ElementA)         ConcreteElementA
visit(ElementB)         ConcreteElementB
```

## Key Benefits
- Easy to add new operations
- Groups related operations together
- Can accumulate state while visiting elements
- Follows Open/Closed Principle

## Common Mistakes
- Breaking encapsulation by exposing element internals
- Creating circular dependencies between visitor and elements
- Over-complicating simple hierarchies

## Interview Questions
1. What is double dispatch in Visitor pattern?
2. How does Visitor differ from Strategy pattern?
3. What happens when new element types are added?
4. When would you NOT use the Visitor pattern?

## Performance

Visitor adds double-dispatch overhead (~10-20ns per visit). For AST traversal with thousands of nodes, this adds up. The benefit is extensibility — new operations without modifying element classes. Compiler passes, code generators, and type checkers use visitors efficiently. The JVM's `instanceof` checks are fast (~2ns) but repeated checks in visitors add up.

## Examples

```java
// Document structure visitor
interface DocumentElement {
    void accept(DocumentVisitor visitor);
}

class Paragraph implements DocumentElement {
    private final String text;
    Paragraph(String text) { this.text = text; }
    
    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }
    
    String getText() { return text; }
}

class Image implements DocumentElement {
    private final String url;
    private final int width;
    Image(String url, int width) { this.url = url; this.width = width; }
    
    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }
    
    String getUrl() { return url; }
    int getWidth() { return width; }
}

interface DocumentVisitor {
    void visit(Paragraph paragraph);
    void visit(Image image);
}

class HtmlExportVisitor implements DocumentVisitor {
    @Override
    public void visit(Paragraph p) {
        System.out.println("<p>" + p.getText() + "</p>");
    }
    @Override
    public void visit(Image img) {
        System.out.println("<img src=\"" + img.getUrl() + "\" width=\"" + img.getWidth() + "\">");
    }
}

class WordCountVisitor implements DocumentVisitor {
    private int count = 0;
    
    @Override
    public void visit(Paragraph p) {
        count += p.getText().split("\\s+").length;
    }
    @Override
    public void visit(Image img) { /* no words */ }
    
    int getCount() { return count; }
}

// Usage
List<DocumentElement> doc = List.of(
    new Paragraph("Hello world"),
    new Image("photo.jpg", 800)
);

HtmlExportVisitor html = new HtmlExportVisitor();
doc.forEach(e -> e.accept(html));

WordCountVisitor wc = new WordCountVisitor();
doc.forEach(e -> e.accept(wc));
System.out.println("Words: " + wc.getCount());
```

## Internal Working

The visitor uses double dispatch: element.accept(visitor) dispatches on the element type, then visitor.visit(element) dispatches on the visitor type. This lets the visitor execute type-specific code without casting. Each element class implements accept() to call the appropriate visit() overload on the visitor. Adding a new operation means creating a new visitor — no changes to element classes.

## Why This Concept Exists

When you have a stable class hierarchy (AST nodes, document elements) but need to add many operations (printing, exporting, analyzing), putting all operations in the element classes violates single responsibility. Visitor extracts each operation into a separate visitor class. Adding a new operation means adding a new visitor — the element classes remain unchanged. This follows open/closed principle for operations.

## Pitfalls

1. **Breaks encapsulation**: Visitor accesses element internals — elements must expose data
2. **Adding elements is hard**: New element type requires updating ALL visitors
3. **Double dispatch complexity**: The accept/visit pattern is non-obvious to new developers
4. **Circular dependency**: Visitor depends on elements, elements depend on visitor interface
5. **Overuse**: Simple hierarchies with few operations don't need visitor

## References

- [Refactoring.Guru - Visitor Pattern](https://refactoring.guru/design-patterns/visitor)
- [Head First Design Patterns - Visitor Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Compiler AST Visitor](https://docs.oracle.com/en/java/javase/21/docs/api/jdk.compiler/com/sun/source/tree/TreeScanner.html)
