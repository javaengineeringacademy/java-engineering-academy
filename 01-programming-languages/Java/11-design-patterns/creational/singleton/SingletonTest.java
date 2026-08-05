package academy.javaengineering.patterns.creational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SingletonTest {

    @Test
    void doubleCheckedLockingReturnsSameInstance() {
        Singleton s1 = Singleton.getInstance("first");
        Singleton s2 = Singleton.getInstance("second");
        assertSame(s1, s2);
    }

    @Test
    void staticHolderReturnsSameInstance() {
        Singleton s1 = Singleton.getHolderInstance();
        Singleton s2 = Singleton.getHolderInstance();
        assertSame(s1, s2);
    }

    @Test
    void staticHolderReturnsData() {
        Singleton s = Singleton.getHolderInstance();
        assertEquals("default", s.getData());
    }

    @Test
    void enumSingletonReturnsSameInstance() {
        EnumSingleton e1 = EnumSingleton.INSTANCE;
        EnumSingleton e2 = EnumSingleton.INSTANCE;
        assertSame(e1, e2);
    }

    @Test
    void enumSingletonMaintainsState() {
        EnumSingleton.INSTANCE.reset();
        EnumSingleton.INSTANCE.setConfig("prod-config");
        EnumSingleton.INSTANCE.incrementConnections();
        assertEquals("prod-config", EnumSingleton.INSTANCE.getConfig());
        assertEquals(1, EnumSingleton.INSTANCE.getConnectionCount());
    }

    @Test
    void doubleCheckedLockingAllowsUpdate() {
        Singleton s = Singleton.getInstance("initial");
        s.setData("updated");
        assertEquals("updated", s.getData());
    }
}
