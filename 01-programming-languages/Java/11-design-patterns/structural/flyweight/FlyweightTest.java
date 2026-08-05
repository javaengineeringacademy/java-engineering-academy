package academy.javaengineering.patterns.structural.flyweight;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlyweightTest {

    @Test
    void testFlyweightFactoryCreation() {
        FlyweightFactory factory = new FlyweightFactory();
        assertNotNull(factory);
    }

    @Test
    void testGetFlyweight() {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight fw = factory.getFlyweight("type1");
        assertNotNull(fw);
        assertEquals("type1", fw.getType());
    }

    @Test
    void testFlyweightReusage() {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight fw1 = factory.getFlyweight("type1");
        Flyweight fw2 = factory.getFlyweight("type1");
        assertSame(fw1, fw2);
    }

    @Test
    void testFlyweightCount() {
        FlyweightFactory factory = new FlyweightFactory();
        factory.getFlyweight("type1");
        factory.getFlyweight("type2");
        assertEquals(2, factory.getFlyweightCount());
    }

    @Test
    void testConcreteFlyweight() {
        ConcreteFlyweight fw = new ConcreteFlyweight("test", "state");
        assertEquals("test", fw.getType());
        assertEquals("state", fw.getIntrinsicState());
    }
}
