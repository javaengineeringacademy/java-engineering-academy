package academy.javaengineering.patterns.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the Prototype design pattern for cloning objects.
 *
 * <p>The Prototype pattern creates new objects by cloning an existing instance
 * (the prototype). It avoids costly object creation by copying existing objects.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Cloneable interface for prototype objects</li>
 *   <li>Deep copy of mutable fields</li>
 *   <li>Creating variations from a template</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class PrototypeExample {

    /**
     * Document class that supports cloning as a prototype.
     */
    public static class Document implements Cloneable {
        private String title;
        private List<String> paragraphs;

        /**
         * Creates a document with the specified title.
         *
         * @param title the document title
         */
        public Document(String title) {
            this.title = title;
            this.paragraphs = new ArrayList<>();
        }

        @Override
        public Document clone() {
            try {
                Document cloned = (Document) super.clone();
                cloned.paragraphs = new ArrayList<>(this.paragraphs);
                return cloned;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }

        /**
         * Adds a paragraph to the document.
         *
         * @param text the paragraph text
         */
        public void addParagraph(String text) {
            paragraphs.add(text);
        }

        /**
         * Creates a copy with a new title.
         *
         * @param newTitle the new title
         * @return a cloned document with the new title
         */
        public Document copyWithTitle(String newTitle) {
            Document copy = this.clone();
            copy.title = newTitle;
            return copy;
        }

        @Override
        public String toString() {
            return "Document{title='" + title + "', paragraphs=" + paragraphs.size() + "}";
        }
    }

    /**
     * Demonstrates prototype pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Document template = new Document("Template");
        template.addParagraph("Header");
        template.addParagraph("Body");

        Document doc1 = template.copyWithTitle("Report 1");
        Document doc2 = template.copyWithTitle("Report 2");

        System.out.println("Template: " + template);
        System.out.println("Doc1: " + doc1);
        System.out.println("Doc2: " + doc2);
    }
}
