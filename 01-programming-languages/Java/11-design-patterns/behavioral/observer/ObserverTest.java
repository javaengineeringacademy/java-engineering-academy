package academy.javaengineering.patterns.behavioral.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    private NewsAgency agency;
    private NewsReader reader1;
    private NewsReader reader2;

    @BeforeEach
    void setUp() {
        agency = new NewsAgency();
        reader1 = new NewsReader("Reader1");
        reader2 = new NewsReader("Reader2");
    }

    @Test
    void shouldNotifyAllAttachedObservers() {
        agency.attach(reader1);
        agency.attach(reader2);

        agency.publishNews("Test News");

        assertEquals("Test News", reader1.getReceivedNews());
        assertEquals("Test News", reader2.getReceivedNews());
    }

    @Test
    void shouldNotNotifyDetachedObservers() {
        agency.attach(reader1);
        agency.attach(reader2);
        agency.detach(reader1);

        agency.publishNews("Test News");

        assertNull(reader1.getReceivedNews());
        assertEquals("Test News", reader2.getReceivedNews());
    }

    @Test
    void shouldStoreLatestNews() {
        agency.publishNews("Latest News");
        assertEquals("Latest News", agency.getLatestNews());
    }

    @Test
    void shouldAttachMultipleObservers() {
        agency.attach(reader1);
        agency.attach(reader2);
        agency.publishNews("Broadcast");
        assertEquals("Broadcast", reader1.getReceivedNews());
        assertEquals("Broadcast", reader2.getReceivedNews());
    }
}
