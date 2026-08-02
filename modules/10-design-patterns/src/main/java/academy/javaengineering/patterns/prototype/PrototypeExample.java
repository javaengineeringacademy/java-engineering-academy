package academy.javaengineering.patterns.prototype;

import java.util.ArrayList;
import java.util.List;

public class PrototypeExample {

    public static class Document implements Cloneable {
        private String title;
        private List<String> paragraphs;

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

        public void addParagraph(String text) {
            paragraphs.add(text);
        }

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
