# Visitor Pattern

The Visitor pattern lets you add further operations to objects without modifying them. It separates an algorithm from the object structure it operates on.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Visitor](#basic-visitor)
3. [Double Dispatch](#double-dispatch)
4. [AST Traversal](#ast-traversal)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Visitor?

Visitor defines a new operation without changing the classes of elements.

```
Visitor ──▶ visit(ElementA)
         ──▶ visit(ElementB)

Element ──▶ accept(Visitor)
```

### When to Use

- Many unrelated operations on object structure
- Operations depend on concrete types
- Avoid polluting element classes

---

## Basic Visitor

### Document Elements

```java
// Visitor interface
public interface DocumentVisitor {
    void visit(Paragraph paragraph);
    void visit(Image image);
    void visit(Table table);
}

// Element interface
public interface DocumentElement {
    void accept(DocumentVisitor visitor);
}

// Concrete elements
public class Paragraph implements DocumentElement {
    private final String text;

    public Paragraph(String text) { this.text = text; }

    @Override
    public void accept(DocumentVisitor visitor) { visitor.visit(this); }

    public String getText() { return text; }
}

public class Image implements DocumentElement {
    private final String url;
    private final int width, height;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Override
    public void accept(DocumentVisitor visitor) { visitor.visit(this); }

    public String getUrl() { return url; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

public class Table implements DocumentElement {
    private final int rows, columns;

    public Table(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public void accept(DocumentVisitor visitor) { visitor.visit(this); }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }
}

// Concrete visitors
public class HtmlExportVisitor implements DocumentVisitor {
    private final StringBuilder html = new StringBuilder();

    @Override
    public void visit(Paragraph paragraph) {
        html.append("<p>").append(paragraph.getText()).append("</p>\n");
    }

    @Override
    public void visit(Image image) {
        html.append("<img src=\"").append(image.getUrl())
            .append("\" width=\"").append(image.getWidth())
            .append("\" height=\"").append(image.getHeight())
            .append("\" />\n");
    }

    @Override
    public void visit(Table table) {
        html.append("<table rows=\"").append(table.getRows())
            .append("\" cols=\"").append(table.getColumns())
            .append("\" />\n");
    }

    public String getHtml() { return html.toString(); }
}

public class WordCountVisitor implements DocumentVisitor {
    private int wordCount = 0;

    @Override
    public void visit(Paragraph paragraph) {
        wordCount += paragraph.getText().split("\\s+").length;
    }

    @Override
    public void visit(Image image) { /* Images have 0 words */ }

    @Override
    public void visit(Table table) { /* Tables have 0 words */ }

    public int getWordCount() { return wordCount; }
}

// Usage
List<DocumentElement> doc = List.of(
    new Paragraph("Hello world"),
    new Image("photo.jpg", 800, 600),
    new Paragraph("More text here"),
    new Table(3, 4)
);

HtmlExportVisitor htmlVisitor = new HtmlExportVisitor();
doc.forEach(e -> e.accept(htmlVisitor));
System.out.println(htmlVisitor.getHtml());

WordCountVisitor wcVisitor = new WordCountVisitor();
doc.forEach(e -> e.accept(wcVisitor));
System.out.println("Words: " + wcVisitor.getWordCount());
```

---

## Double Dispatch

### How Double Dispatch Works

```java
// accept() dispatches on element type
// visit() dispatches on visitor type

public class Circle implements Shape {
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);  // dispatches to visit(Circle)
    }
}

public class Rectangle implements Shape {
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);  // dispatches to visit(Rectangle)
    }
}

// Two dispatches:
// 1. element.accept(visitor) - chooses element's accept
// 2. visitor.visit(element) - chooses visitor's visit method
```

---

## AST Traversal

### Expression Tree Visitor

```java
public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor);
}

public record Number(double value) implements Expression {
    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitNumber(this);
    }
}

public record Add(Expression left, Expression right) implements Expression {
    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitAdd(this);
    }
}

public record Multiply(Expression left, Expression right) implements Expression {
    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitMultiply(this);
    }
}

public interface ExpressionVisitor<T> {
    T visitNumber(Number number);
    T visitAdd(Add add);
    T visitMultiply(Multiply multiply);
}

// Evaluator visitor
public class Evaluator implements ExpressionVisitor<Double> {
    @Override
    public Double visitNumber(Number number) { return number.value(); }

    @Override
    public Double visitAdd(Add add) {
        return add.left().accept(this) + add.right().accept(this);
    }

    @Override
    public Double visitMultiply(Multiply multiply) {
        return multiply.left().accept(this) * multiply.right().accept(this);
    }
}

// Pretty printer visitor
public class PrettyPrinter implements ExpressionVisitor<String> {
    @Override
    public String visitNumber(Number number) {
        return String.valueOf(number.value());
    }

    @Override
    public String visitAdd(Add add) {
        return "(" + add.left().accept(this) + " + " + add.right().accept(this) + ")";
    }

    @Override
    public String visitMultiply(Multiply multiply) {
        return "(" + multiply.left().accept(this) + " * " + multiply.right().accept(this) + ")";
    }
}

// Usage: (3 + 5) * 2
Expression expr = new Multiply(
    new Add(new Number(3), new Number(5)),
    new Number(2)
);

System.out.println(expr.accept(new Evaluator()));        // 16.0
System.out.println(expr.accept(new PrettyPrinter()));   // ((3.0 + 5.0) * 2.0)
```

---

## Best Practices

### Do

```java
// 1. Keep visitor interface focused
public interface Visitor<T> {
    T visitA(A a);
    T visitB(B b);
}

// 2. Use for operations, not data
// Data stays in elements, operations in visitors
```

### Don't

```java
// 1. Don't use when adding new element types is frequent
// Each new element requires updating all visitors

// 2. Don't break encapslement
// Visitors need access to element internals
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Visitor** | Add operations without modifying elements |
| **accept** | Element accepts visitor |
| **visit** | Visitor processes element |
| **Double Dispatch** | Two dispatches: accept + visit |
| **Extensibility** | Add new operations easily |
| **Encapsulation** | Operations separate from data |
| **Use Cases** | AST, compilers, document processing |
