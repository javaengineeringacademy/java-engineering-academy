package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class HibernateFundamentalsTest {

    private HibernateFundamentalsExample.SessionSimulator session;

    @BeforeEach
    void setUp() {
        session = new HibernateFundamentalsExample.SessionSimulator();
    }

    @Test
    void shouldSaveAndFind() {
        HibernateFundamentalsExample.Entity entity = new HibernateFundamentalsExample.Entity(1L, "John", "john@test.com");
        session.save(entity);
        assertNotNull(session.findById(1L));
        assertEquals("John", session.findById(1L).getName());
    }

    @Test
    void shouldDelete() {
        HibernateFundamentalsExample.Entity entity = new HibernateFundamentalsExample.Entity(1L, "John", "john@test.com");
        session.save(entity);
        session.delete(1L);
        assertNull(session.findById(1L));
    }

    @Test
    void shouldFindAll() {
        session.save(new HibernateFundamentalsExample.Entity(1L, "John", "john@test.com"));
        session.save(new HibernateFundamentalsExample.Entity(2L, "Jane", "jane@test.com"));
        assertEquals(2, session.findAll().size());
    }
}
