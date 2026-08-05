package academy.javaengineering.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Subject implementation - News Agency.
 * Maintains a list of observers and sends news updates to them.
 */
public class NewsAgency implements Subject {

    private final List<Observer> observers = new ArrayList<>();
    private String latestNews;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Publish breaking news to all subscribers.
     *
     * @param news the news headline to publish
     */
    public void publishNews(String news) {
        this.latestNews = news;
        System.out.println("News Agency: Breaking - " + news);
        notifyObservers(news);
    }

    public String getLatestNews() {
        return latestNews;
    }
}
