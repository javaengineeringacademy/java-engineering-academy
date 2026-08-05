package academy.javaengineering.patterns.behavioral.observer;

/**
 * Concrete Observer implementation - News Reader.
 * Receives and processes news updates from the NewsAgency.
 */
public class NewsReader implements Observer {

    private final String name;
    private String receivedNews;

    public NewsReader(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        this.receivedNews = message;
        System.out.println(name + " received: " + message);
    }

    public String getName() {
        return name;
    }

    public String getReceivedNews() {
        return receivedNews;
    }
}
