package academy.javaengineering.patterns.proxy;

import academy.javaengineering.patterns.proxy.ProxyExample.Database;
import academy.javaengineering.patterns.proxy.ProxyExample.VirtualProxy;
import academy.javaengineering.patterns.proxy.ProxyExample.RealDatabase;
import academy.javaengineering.patterns.proxy.ProxyExample.ProtectionProxy;
import academy.javaengineering.patterns.proxy.ProxyExample.CachingProxy;
import academy.javaengineering.patterns.proxy.ProxyExample.LoggingProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPatternTest {

    @Test
    @DisplayName("VirtualProxy should implement Database interface")
    void proxyShouldImplementDatabaseInterface() {
        Database proxy = new VirtualProxy("test.db");
        assertInstanceOf(Database.class, proxy);
    }

    @Test
    @DisplayName("RealDatabase should implement Database interface")
    void realDatabaseShouldImplementDatabaseInterface() {
        RealDatabase real = new RealDatabase("test.db");
        assertInstanceOf(Database.class, real);
    }

    @Test
    @DisplayName("VirtualProxy should not load database on construction")
    void proxyShouldNotLoadOnConstruction() {
        VirtualProxy proxy = new VirtualProxy("test.db");
        assertNull(getRealDatabaseField(proxy),
                "VirtualProxy should not create RealDatabase until query() is called");
    }

    @Test
    @DisplayName("VirtualProxy should load database on first query")
    void proxyShouldLoadOnFirstQuery() {
        VirtualProxy proxy = new VirtualProxy("test.db");
        proxy.query("SELECT 1");
        assertNotNull(getRealDatabaseField(proxy),
                "After query(), RealDatabase should be created");
    }

    @Test
    @DisplayName("VirtualProxy should not reload on subsequent queries")
    void proxyShouldNotReloadOnSubsequentQuery() {
        VirtualProxy proxy = new VirtualProxy("test.db");
        proxy.query("SELECT 1");
        Object first = getRealDatabaseField(proxy);
        proxy.query("SELECT 2");
        Object second = getRealDatabaseField(proxy);
        assertSame(first, second,
                "VirtualProxy should reuse the same RealDatabase instance");
    }

    @Test
    @DisplayName("Should not throw on query")
    void shouldNotThrowOnQuery() {
        Database proxy = new VirtualProxy("test.db");
        assertDoesNotThrow(() -> proxy.query("SELECT 1"));
    }

    @Test
    @DisplayName("RealDatabase should not throw on query")
    void realDatabaseShouldNotThrowOnQuery() {
        RealDatabase real = new RealDatabase("test.db");
        assertDoesNotThrow(() -> real.query("SELECT 1"));
    }

    @Test
    @DisplayName("VirtualProxy and RealDatabase should both be usable through Database interface")
    void bothShouldBeUsableThroughInterface() {
        Database proxy = new VirtualProxy("test.db");
        Database real = new RealDatabase("test.db");
        assertDoesNotThrow(() -> proxy.query("SELECT 1"));
        assertDoesNotThrow(() -> real.query("SELECT 1"));
    }

    @Test
    @DisplayName("ProtectionProxy should deny non-admin DELETE")
    void protectionProxyShouldDenyNonAdmin() {
        RealDatabase real = new RealDatabase("secure.db");
        Database userProxy = new ProtectionProxy(real, "user");
        String result = userProxy.query("DELETE FROM users WHERE id=1");
        assertEquals("ACCESS DENIED", result);
    }

    @Test
    @DisplayName("ProtectionProxy should allow admin DELETE")
    void protectionProxyShouldAllowAdmin() {
        RealDatabase real = new RealDatabase("secure.db");
        Database adminProxy = new ProtectionProxy(real, "admin");
        String result = adminProxy.query("DELETE FROM users WHERE id=1");
        assertEquals("Result for: DELETE FROM users WHERE id=1", result);
    }

    @Test
    @DisplayName("CachingProxy should return cached result on second call")
    void cachingProxyShouldCache() {
        RealDatabase real = new RealDatabase("cache.db");
        Database cachingProxy = new CachingProxy(real);
        String first = cachingProxy.query("SELECT * FROM users");
        String second = cachingProxy.query("SELECT * FROM users");
        assertEquals(first, second);
    }

    @Test
    @DisplayName("CachingProxy should return different results for different queries")
    void cachingProxyShouldReturnDifferentForDifferentQueries() {
        RealDatabase real = new RealDatabase("cache.db");
        Database cachingProxy = new CachingProxy(real);
        String first = cachingProxy.query("SELECT * FROM users");
        String second = cachingProxy.query("SELECT * FROM orders");
        assertNotEquals(first, second);
    }

    private Object getRealDatabaseField(VirtualProxy proxy) {
        try {
            var field = VirtualProxy.class.getDeclaredField("realDatabase");
            field.setAccessible(true);
            return field.get(proxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
